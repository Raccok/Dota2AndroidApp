package github.com.rhacco.dota2androidapp.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.base.BaseLifecycleFragment
import github.com.rhacco.dota2androidapp.lists.TopMatchesAdapter
import github.com.rhacco.dota2androidapp.lists.TopMatchesItemData
import github.com.rhacco.dota2androidapp.viewmodel.TopMatchesViewModel
import kotlinx.android.synthetic.main.recycler_view_swipe_refresh.*

class TopMatchesFragment : BaseLifecycleFragment<TopMatchesViewModel>() {
    override val mViewModelClass = TopMatchesViewModel::class.java
    private lateinit var mAdapter: TopMatchesAdapter

    override fun onCreateView(
            inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.recycler_view_swipe_refresh, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        mAdapter = TopMatchesAdapter(context)
        recycler_view.adapter = mAdapter
        val layoutManager = LinearLayoutManager(context)
        recycler_view.layoutManager = layoutManager
        recycler_view.addItemDecoration(
                DividerItemDecoration(recycler_view.context, layoutManager.orientation))
        val tabPosition = arguments["tab_position"]
        swipe_refresh_layout.setOnRefreshListener {
            if (tabPosition == 0)
                mViewModel.getTopLiveMatches()
            else
                mViewModel.getTopRecentMatches()
            swipe_refresh_layout.isRefreshing = false
        }
        observeLiveData()
        if (tabPosition == 0)
            mViewModel.getTopLiveMatches()
        else
            mViewModel.getTopRecentMatches()
    }

    override fun observeLiveData() {
        mViewModel.mGetTopMatchesQuery.observe(this, Observer<List<TopMatchesItemData>> {
            it?.let { newTopMatches -> mAdapter.update(newTopMatches) }
        })
    }
}