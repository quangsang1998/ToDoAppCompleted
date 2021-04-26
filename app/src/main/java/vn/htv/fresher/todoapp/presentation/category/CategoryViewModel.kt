package vn.htv.fresher.todoapp.presentation.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import vn.htv.fresher.todoapp.data.db.entity.Task
import vn.htv.fresher.todoapp.domain.model.CategoryModel
import vn.htv.fresher.todoapp.domain.model.TaskModel
import vn.htv.fresher.todoapp.domain.usecase.category.GetCategoryListUseCase
import vn.htv.fresher.todoapp.domain.usecase.category.SaveCategoryUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.GetTaskListUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.SaveTaskUseCase
import vn.htv.fresher.todoapp.presentation.common.BaseViewModel

class CategoryViewModel(
    //private val getCategoryListUseCase: GetCategoryListUseCase,
    private val getTaskListUseCase: GetTaskListUseCase,
    private val saveCategoryUseCase: SaveCategoryUseCase,
    private val saveTaskUseCase: SaveTaskUseCase
) : BaseViewModel() {

  val itemList: LiveData<List<TaskModel>> get() = _itemList
  private val _itemList = MutableLiveData<List<TaskModel>>()
//  val catItemList: LiveData<List<CategoryModel>> get() = _catItemList
//  private val _catItemList = MutableLiveData<List<CategoryModel>>()
 fun loadData() {
//   val list = mutableListOf<TaskModel>()
//   list.addAll()
   disposables += getTaskListUseCase()
       .subscribeBy(
           onSuccess = {
             _itemList.postValue(it)
           },
           onError = {
             Timber.e("error")
           }
       )
//  disposables += getCategoryListUseCase()
//      .subscribeBy(
//           onSuccess = {
//            _catItemList.postValue(it)
//          },
//          onError = {
//            Timber.e("error")
//          }
//      )
  }
//  fun saveTaskList(): List<TaskModel>{
//    var list = ArrayList<TaskModel>()
//    list.add(TaskModel(
//        id = 1,
//        catId = 1,
//        name = "",
//        finished = true,
//        deadline = LocalDateTime.now(),
//        myDay = true,
//        important = true,
//        reminder = LocalDateTime.now(),
//        repeat = 1,
//        createdAt = LocalDateTime.now(),
//        note = ""))
//    return  list
//  }
  fun insertCategory() {
    val model = CategoryModel(
        name = "category",
        id = 1,
        icon = "",
        createdAt = LocalDateTime.now()
    )
  disposables += saveCategoryUseCase(model)
      .subscribeBy(
          onComplete = {
            Timber.i("saved task success")
          },
          onError = {
            Timber.e(" error")
          }
      )
  }
  fun insertTask() {
  val model = TaskModel(
      name = "task",
      catId = 1,
      finished = false,
      deadline = LocalDateTime.now(),
      myDay = true,
      important = true,
      reminder = LocalDateTime.now(),
      repeat = 1,
      createdAt = LocalDateTime.now(),
      note = ""
  )
  disposables += saveTaskUseCase(model)
      .subscribeBy(
          onComplete = {
            Timber.i("saved task success")
          },
          onError =
          {
            Timber.e(" error")
          }
      )
  }
}