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
import github.com.rhacco.dotascoop.lists.HeroesAdapter
import github.com.rhacco.dotascoop.sources.databases.entities.HeroEntity
import github.com.rhacco.dotascoop.viewmodel.HeroesViewModel
import kotlinx.android.synthetic.main.recycler_view_drawer.*

class HeroesActivity : BaseLifecycleActivity<HeroesViewModel>() {
    override val mViewModelClass = HeroesViewModel::class.java
    private lateinit var mAdapter: HeroesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler_view_drawer)
        super.initNavigationDrawer(drawer_layout)
        mAdapter = HeroesAdapter(this)
        recycler_view.adapter = mAdapter
        val layoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = layoutManager
        recycler_view.addItemDecoration(
                DividerItemDecoration(recycler_view.context, layoutManager.orientation))
        observeLiveData()
        mViewModel.getHeroes()
        handleIntent(intent)
    }

    override fun observeLiveData() {
        mViewModel.mGetHeroes.observe(this, Observer<List<HeroEntity>> {
            it?.let { heroes -> mAdapter.update(heroes) }
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