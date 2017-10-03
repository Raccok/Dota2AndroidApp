package github.com.rhacco.dota2androidapp.base

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.utilities.deviceIsOnline

@Suppress("LeakingThis")
abstract class BaseLifecycleActivity<T : AndroidViewModel> : AppCompatActivity(), LifecycleRegistryOwner {
    abstract val mViewModelClass: Class<T>
    protected val mViewModel: T by lazy { ViewModelProviders.of(this).get(mViewModelClass) }
    private val mRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry = mRegistry

    // Observe actions on data fetched from the respective API and react accordingly.
    @Override
    protected open fun observeLiveData() {
        if (!deviceIsOnline(getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?)) {
            Toast.makeText(applicationContext,
                    resources.getString(R.string.error_querying_api_failed) + " no internet connection",
                    Toast.LENGTH_LONG).show()
            return
        }
    }
}
