package github.com.rhacco.dotascoop.base

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.ViewModelProviders

@Suppress("LeakingThis")
abstract class BaseLifecycleActivity<T : AndroidViewModel> :
        BaseNavigationDrawerActivity(), LifecycleRegistryOwner {
    abstract val mViewModelClass: Class<T>
    protected val mViewModel: T by lazy { ViewModelProviders.of(this).get(mViewModelClass) }
    private val mRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry = mRegistry

    // Observe actions on data stored and managed by the related ViewModel and react accordingly.
    abstract fun observeLiveData()
}