package github.com.raccok.dota2androidapp.base

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity

@Suppress("LeakingThis")
abstract class BaseLifecycleActivity<T : AndroidViewModel> : AppCompatActivity(), LifecycleRegistryOwner {
    abstract val mViewModelClass: Class<T>
    // TODO(Rhacco): mViewModel seems to be unused - remove it or is it useful for later?
    protected val mViewModel: T by lazy { ViewModelProviders.of(this).get(mViewModelClass) }
    private val mRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry = mRegistry
}
