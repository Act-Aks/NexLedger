package com.actaks.nexledger.feature.categories.presentation.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actaks.nexledger.core.database.repository.CategoryRepository
import com.actaks.nexledger.core.model.Category
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryFormViewModel(
    private val categoryRepository: CategoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(CategoryFormState())
    val state: StateFlow<CategoryFormState> = _state.asStateFlow()

    private val _events = Channel<CategoryFormEvent>()
    val events = _events.receiveAsFlow()

    init {
        val categoryId = savedStateHandle.get<Long>("categoryId") ?: 0L
        if (categoryId > 0) {
            _state.update { it.copy(isEditing = true) }
            viewModelScope.launch {
                categoryRepository.getById(categoryId)?.let { cat ->
                    _state.update {
                        it.copy(
                            name = cat.name,
                            type = cat.type,
                            color = cat.color,
                            icon = cat.icon
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: CategoryFormAction) {
        when (action) {
            is CategoryFormAction.OnBackNavigationClick -> navigateBack()
            is CategoryFormAction.OnNameChange -> _state.update { it.copy(name = action.name) }
            is CategoryFormAction.OnTypeSelect -> _state.update { it.copy(type = action.type) }
            is CategoryFormAction.OnColorSelect -> _state.update { it.copy(color = action.color) }
            CategoryFormAction.OnSave -> save()
            CategoryFormAction.OnErrorDismissed -> _state.update { it.copy(error = null) }
        }
    }

    private fun save() {
        val data = _state.value
        if (data.name.isBlank()) {
            _state.update { it.copy(error = "Name is required") }
            return
        }
        _state.update { it.copy(saving = true) }
        viewModelScope.launch {
            try {
                categoryRepository.create(
                    Category(
                        name = data.name,
                        type = data.type,
                        icon = data.icon,
                        color = data.color
                    )
                )
                _state.update { it.copy(saving = false, saved = true) }
            } catch (e: Exception) {
                _state.update { it.copy(saving = false, error = e.message) }
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(CategoryFormEvent.NavigateBack)
        }
    }
}
