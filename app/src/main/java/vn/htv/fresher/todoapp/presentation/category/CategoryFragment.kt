package vn.htv.fresher.todoapp.presentation.category

import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import kotlinx.android.synthetic.main.fragment_category.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.threeten.bp.LocalDateTime
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.databinding.FragmentCategoryBinding
import vn.htv.fresher.todoapp.domain.model.TaskModel
import vn.htv.fresher.todoapp.presentation.common.BaseFragment

class CategoryFragment : BaseFragment<FragmentCategoryBinding>() {

  private val viewModel by sharedViewModel<CategoryViewModel>()

  override val layoutId  : Int
  get() = R.layout.fragment_category

  private val taskAdapter by lazy {
    TaskAdapter(
      importantCallback = {
        viewModel.updateImportant(it)
      },
      finishedCallback = {
        viewModel.updateFinished(it)
      }
    )
  }

  override fun init() {
    super.init()
    viewModel.loadCategory()
    viewModel.loadTask()
    binding.event = EventAddTask()
  }

  override fun initUi() {
    super.initUi()
    taskRecycleView.apply {
      adapter = taskAdapter
    }
  }

  override fun registerLiveDataListener() {
    super.registerLiveDataListener()
      viewModel.itemList.observe(this, {
           taskAdapter.setItems(it)
         }
      )

    viewModel.updateTaskCompleted.observe(this, {
             viewModel.loadTask()
       }
    )

    viewModel.updateCategoryCompleted.observe(this, {
             viewModel.loadCategory()
       }
    )

    viewModel.deleteCategoryCompleted.observe(this, {
             safeActivity.onBackPressed()
             viewModel.loadCategory()
       }
    )

    viewModel.itemCategory.observe(this, {
             safeActivity.supportActionBar?.title = it.name
       }
    )

    viewModel.addTaskCompleted.observe(this@CategoryFragment, Observer {
             if (!it) return@Observer
             viewModel.loadTask()
       }
    )
  }

  inner class EventAddTask() {
    fun onNewTask() {
      MaterialDialog(safeContext).show {
        input(
          hint = resources.getString(R.string.new_task_hint),
        ) { _, title ->
            val model = TaskModel(
              name       = title.toString(),
              catId      = viewModel.categoryId?.toInt(),
              createdAt  = LocalDateTime.now()
            )
            viewModel.addNewTask(model)
          }
        positiveButton(R.string.button_create_task)
        negativeButton(R.string.button_delete)
      }
    }
  }

  companion object {
    fun newInstance() = CategoryFragment()
  }
}
