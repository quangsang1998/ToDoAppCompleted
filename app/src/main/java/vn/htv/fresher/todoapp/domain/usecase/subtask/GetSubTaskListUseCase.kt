package vn.htv.fresher.todoapp.domain.usecase.subtask

import io.reactivex.Single
import vn.htv.fresher.todoapp.domain.model.SubTaskModel
import vn.htv.fresher.todoapp.domain.repository.SubTaskRepository

class GetSubTaskListUseCase(
  private val subTaskRepository: SubTaskRepository
) {
  operator fun invoke(id: Int? = null): Single<List<SubTaskModel>> {
    return subTaskRepository.getSubTaskList(id)
  }
}