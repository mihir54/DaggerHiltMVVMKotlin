package com.mihir.daggerhiltmvvmkotlin.utils

import androidx.appcompat.widget.SearchView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object RxSearchObservable {

    fun fromView(searchView: SearchView): Observable<String?>? {
        val subject: PublishSubject<String?> = PublishSubject.create()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                subject.onComplete()
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                text?.let { subject.onNext(it) }
                return true
            }
        })
        return subject
    }

}