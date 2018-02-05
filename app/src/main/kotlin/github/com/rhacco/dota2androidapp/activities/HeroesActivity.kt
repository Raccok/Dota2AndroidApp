package github.com.rhacco.dota2androidapp.activities

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.base.BaseLifecycleActivity
import github.com.rhacco.dota2androidapp.lists.HeroesAdapter
import github.com.rhacco.dota2androidapp.sources.databases.entities.HeroEntity
import github.com.rhacco.dota2androidapp.viewmodel.HeroesViewModel
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
    }

    override fun observeLiveData() {
        mViewModel.mGetHeroes.observe(this, Observer<List<HeroEntity>> {
            it?.let { heroes -> mAdapter.update(heroes) }
        })
    }
}