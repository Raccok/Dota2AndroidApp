package github.com.rhacco.dota2androidapp.base

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment

@Suppress("LeakingThis")
abstract class BaseLifecycleFragment<T : AndroidViewModel> : Fragment(), LifecycleRegistryOwner {
    abstract val mViewModelClass: Class<T>
    protected val mViewModel: T by lazy { ViewModelProviders.of(this).get(mViewModelClass) }
    private val mRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry = mRegistry

    // Observe actions on data stored and managed by the related ViewModel and react accordingly.
    abstract fun observeLiveData()
}
