package vn.htv.fresher.todoapp.presentation.main

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.subscribeBy
import vn.htv.fresher.todoapp.presentation.common.BaseViewModel
import timber.log.Timber
import io.reactivex.rxkotlin.plusAssign
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.domain.model.CategoryModel
import vn.htv.fresher.todoapp.domain.model.TaskModel
import vn.htv.fresher.todoapp.domain.usecase.category.GetCategoryListUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.*

enum class TaskGroup {
  MY_DAY,
  IMPORTANT,
  DEADLINE,
  ACTION;

  val groupName: Int
    @StringRes get() = when (this) {
      MY_DAY    -> R.string.task_group_my_day
      IMPORTANT -> R.string.task_group_important
      DEADLINE  -> R.string.task_group_deadline
      ACTION    -> R.string.task_group_action
    }

  val groupIcon: Int
    @StringRes get() = when (this) {
      MY_DAY    -> R.drawable.ic_my_day
      IMPORTANT -> R.drawable.ic_important
      DEADLINE  -> R.drawable.ic_deadline
      ACTION    -> R.drawable.ic_action
    }
}

enum class MainItemType(val value: Int) {
  MAIN_ITEM(0),
  SEPARATOR(1);

  companion object {
    fun from(value: Int) = values().first { it.value == value }
  }
}

sealed class MainItem(val type: MainItemType) {
  data class Item(val model: MainItemModel): MainItem(MainItemType.MAIN_ITEM)

  object Separator: MainItem(MainItemType.SEPARATOR)
}

data class MainItemModel(
  val iconId      : Int = R.drawable.ic_category_default,
  val name        : String,
  val taskNumber  : Int = 0
) {
  val hasTask: Boolean get() = taskNumber != 0
}

class MainViewModel(
  private val context                 : Context,
  private val getTaskListUseCase      : GetTaskListUseCase,
  private val getCategoryListUseCase  : GetCategoryListUseCase
) : BaseViewModel() {

  val mainItemList: LiveData<List<MainItem>> get() = _mainItemList
  private val _mainItemList = MutableLiveData<List<MainItem>>()

  fun loadData() {
    val getTaskObservable     = getTaskListUseCase()
    val getCategoryObservable = getCategoryListUseCase()
    val zipper = BiFunction<List<TaskModel>, List<CategoryModel>, List<MainItem>> { tasks, categories ->
      val list = mutableListOf<MainItem>()
      val taskGroupList = generateTaskGroup(tasks)

      list.addAll(taskGroupList)
      list.add(MainItem.Separator)

      val categoryItems = categories.map { category ->
        MainItem.Item(
          MainItemModel(
            name = category.name,
            taskNumber = tasks.filter { it.catId == category.id }.size
          )
        )
      }
      list.addAll(categoryItems)
      list
    }

    disposables += Single.zip(getTaskObservable, getCategoryObservable, zipper)
      .subscribeBy(
        onSuccess = {
          _mainItemList.postValue(it)
        },
        onError = {
          Timber.i("Cannot post value")
        }
      )
  }

  private fun generateTaskGroup(tasks: List<TaskModel>): List<MainItem> {
    val list = mutableListOf<MainItem>()

    val items = TaskGroup.values().map { group ->
      val taskNumber = when (group) {
        TaskGroup.MY_DAY    -> tasks.filter { it.myDay }.size
        TaskGroup.IMPORTANT -> tasks.filter { it.important }.size
        TaskGroup.DEADLINE  -> tasks.filter { it.deadline != null }.size
        TaskGroup.ACTION    -> tasks.filter { it.catId == null }.size
      }

      val mainItemModel = MainItemModel(
        iconId      = group.groupIcon,
        name        = context.getString(group.groupName),
        taskNumber  = taskNumber
      )

      MainItem.Item(mainItemModel)
    }

    list.addAll(items)

    return list
  }
}