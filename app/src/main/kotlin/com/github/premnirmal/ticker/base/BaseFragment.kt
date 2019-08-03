package com.github.premnirmal.ticker.base

import android.content.Context
import android.os.Bundle
import android.util.AndroidRuntimeException
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.github.premnirmal.ticker.analytics.Analytics
import com.github.premnirmal.ticker.components.Injector
import com.trello.rxlifecycle3.android.FragmentEvent
import com.trello.rxlifecycle3.android.FragmentEvent.ATTACH
import com.trello.rxlifecycle3.android.FragmentEvent.CREATE
import com.trello.rxlifecycle3.android.FragmentEvent.CREATE_VIEW
import com.trello.rxlifecycle3.android.FragmentEvent.DESTROY
import com.trello.rxlifecycle3.android.FragmentEvent.DESTROY_VIEW
import com.trello.rxlifecycle3.android.FragmentEvent.DETACH
import com.trello.rxlifecycle3.android.FragmentEvent.PAUSE
import com.trello.rxlifecycle3.android.FragmentEvent.RESUME
import com.trello.rxlifecycle3.android.FragmentEvent.START
import com.trello.rxlifecycle3.android.FragmentEvent.STOP
import com.trello.rxlifecycle3.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

/**
 * Created by premnirmal on 2/25/16.
 */
abstract class BaseFragment : Fragment(), FragmentLifeCycleOwner {

  override val lifecycle: BehaviorSubject<FragmentEvent> = BehaviorSubject.create<FragmentEvent>()

  private var calledSuperOnViewCreated: Boolean = false
  protected val analytics: Analytics
    get() = holder.analytics
  private val holder: InjectionHolder by lazy { InjectionHolder() }

  abstract val simpleName: String

  class InjectionHolder {
    @Inject internal lateinit var analytics: Analytics

    init {
      Injector.appComponent.inject(this)
    }
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    lifecycle.onNext(ATTACH)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    lifecycle.onNext(CREATE)
    analytics.trackScreenView(simpleName)
  }

  @CallSuper
  override fun onViewCreated(
      view: View,
      savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    lifecycle.onNext(CREATE_VIEW)
    calledSuperOnViewCreated = true
  }

  override fun onStart() {
    super.onStart()
    lifecycle.onNext(START)
  }

  override fun onResume() {
    if (!calledSuperOnViewCreated) {
      throw AndroidRuntimeException(
          "You didn't call super.onViewCreated() when in " + simpleName
      )
    }
    super.onResume()
    lifecycle.onNext(RESUME)
  }

  override fun onPause() {
    lifecycle.onNext(PAUSE)
    super.onPause()
  }

  override fun onStop() {
    lifecycle.onNext(STOP)
    super.onStop()
  }

  override fun onDestroyView() {
    lifecycle.onNext(DESTROY_VIEW)
    super.onDestroyView()
  }

  override fun onDetach() {
    lifecycle.onNext(DETACH)
    super.onDetach()
  }

  override fun onDestroy() {
    lifecycle.onNext(DESTROY)
    super.onDestroy()
  }

  /**
   * Using this to automatically unsubscribe from observables on lifecycle events
   */
  protected fun <T> bind(observable: Observable<T>): Observable<T> =
    observable.compose(RxLifecycleAndroid.bindFragment(lifecycle))
}
