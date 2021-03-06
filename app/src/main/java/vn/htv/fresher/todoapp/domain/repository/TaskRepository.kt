package vn.htv.fresher.todoapp.domain.repository

import io.reactivex.Completable
import io.reactivex.Single
import vn.htv.fresher.todoapp.data.db.entity.Task
import vn.htv.fresher.todoapp.domain.model.TaskModel
import java.util.*

interface TaskRepository {
  fun deleteTask(model: TaskModel): Completable

  fun get(id: Int): Single<TaskModel>

  fun getTaskList(catId: Int?): Single<List<TaskModel>>

  fun saveTask(model: TaskModel): Completable

  fun updateTask(model: TaskModel): Completable
}