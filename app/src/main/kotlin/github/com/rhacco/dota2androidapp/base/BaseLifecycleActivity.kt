package github.com.rhacco.dota2androidapp.base

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity

@Suppress("LeakingThis")
abstract class BaseLifecycleActivity<T : AndroidViewModel> : AppCompatActivity(), LifecycleRegistryOwner {
    abstract val mViewModelClass: Class<T>
    protected val mViewModel: T by lazy { ViewModelProviders.of(this).get(mViewModelClass) }
    private val mRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry = mRegistry

    // Observe actions on data fetched from the respective API and react accordingly.
    abstract fun observeLiveData()
}
