package vn.htv.fresher.todoapp.presentation.category

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.task_item.view.*
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.databinding.TaskItemBinding
import vn.htv.fresher.todoapp.domain.model.TaskModel

class TaskAdapter(
  private val finishedCallback   : ((model: TaskModel) -> Unit),
  private val importantCallback  : ((model: TaskModel) -> Unit),
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

  private val taskList = mutableListOf<TaskModel>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
    val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return TaskViewHolder(binding)
  }

  override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
    val task: TaskModel = taskList.get(position)
    holder.bind(task)
  }

  override fun getItemCount(): Int {
    return taskList.size
  }

  fun setItems(items: List<TaskModel>) {
    taskList.clear()
    taskList.addAll(items)
    notifyDataSetChanged()
  }

  inner class TaskViewHolder(itemView: TaskItemBinding): RecyclerView.ViewHolder(itemView.root) {

    var binding: TaskItemBinding = itemView

    fun bind(model: TaskModel) {
      binding.model = model

      binding.root.importantImageView.setImageResource(
        if (model.important) R.drawable.ic_baseline_star_24 else R.drawable.ic_baseline_star_outline_24
      )

      binding.root.importantImageView.setOnClickListener {
        importantCallback.invoke(model)
      }

      binding.root.completeImageView.setImageResource(
        if (model.finished) R.drawable.ic_baseline_check_circle_24 else R.drawable.ic_baseline_brightness_1_24
      )

      binding.root.taskTextView.paintFlags = if (model.finished) Paint.STRIKE_THRU_TEXT_FLAG else 0

      binding.root.completeImageView.setOnClickListener {
        finishedCallback.invoke(model)
      }
    }
  }
}
