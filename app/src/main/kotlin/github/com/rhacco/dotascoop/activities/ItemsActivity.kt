package github.com.rhacco.dotascoop.activities

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import github.com.rhacco.dotascoop.R
import github.com.rhacco.dotascoop.base.BaseLifecycleActivity
import github.com.rhacco.dotascoop.lists.ItemsAdapter
import github.com.rhacco.dotascoop.sources.databases.entities.ItemEntity
import github.com.rhacco.dotascoop.viewmodel.ItemsViewModel
import kotlinx.android.synthetic.main.recycler_view_drawer.*

class ItemsActivity : BaseLifecycleActivity<ItemsViewModel>() {
    override val mViewModelClass = ItemsViewModel::class.java
    private lateinit var mAdapter: ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler_view_drawer)
        super.initNavigationDrawer(drawer_layout)
        mAdapter = ItemsAdapter(this)
        recycler_view.adapter = mAdapter
        val layoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = layoutManager
        recycler_view.addItemDecoration(
                DividerItemDecoration(recycler_view.context, layoutManager.orientation))
        observeLiveData()
        mViewModel.getItems()
        handleIntent(intent)
    }

    override fun observeLiveData() {
        mViewModel.mGetItems.observe(this, Observer<List<ItemEntity>> {
            it?.let { items -> mAdapter.update(items) }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_view, menu)
        val searchMenuItem = menu?.findItem(R.id.search)
        val searchView = searchMenuItem?.actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setPadding(-30, 15, 20, 15)
        searchView.setIconifiedByDefault(false)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnCloseListener { handleSearchClosed() }
        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean = handleSearchClosed()

            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean = true
        })
        return true
    }

    override fun onNewIntent(intent: Intent) = handleIntent(intent)

    private fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_SEARCH)
            mAdapter.handleSearchQuery(intent.getStringExtra(SearchManager.QUERY))
    }

    private fun handleSearchClosed(): Boolean {
        mAdapter.showAllEntries()
        return true
    }
}
