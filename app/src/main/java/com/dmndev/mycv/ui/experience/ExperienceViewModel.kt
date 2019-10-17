package com.dmndev.mycv.ui.experience

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dmndev.mycv.di.Injector
import com.dmndev.mycv.model.realm.Experience
import com.dmndev.mycv.model.repository.MyCVRepository
import com.dmndev.mycv.utils.DisposableManager
import io.reactivex.observers.DisposableObserver
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class ExperienceViewModel : ViewModel() {

    @Inject
    lateinit var myCVRepository: MyCVRepository
    @Inject
    lateinit var disposableManager: DisposableManager

    private val _experienceList = MutableLiveData<List<Experience>>().apply {
        value = arrayListOf()
    }

    val experienceList: LiveData<List<Experience>> = _experienceList

    init {
        Injector.getComponent().inject(this)
        updateAndGetExperience()
    }

    fun updateAndGetExperience() {
        myCVRepository.updateExperience()

        myCVRepository.getExperience()
            .subscribeWith(object : DisposableObserver<List<Experience>>() {
                override fun onComplete() {
                }

                override fun onNext(t: List<Experience>) {
                    _experienceList.apply {
                        value = t
                    }
                }

                override fun onError(e: Throwable) {
                }
            }).addTo(disposableManager.compositeDisposable)
    }
}