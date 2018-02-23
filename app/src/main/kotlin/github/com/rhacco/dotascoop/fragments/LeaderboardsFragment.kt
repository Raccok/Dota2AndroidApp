package github.com.rhacco.dotascoop.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.com.rhacco.dotascoop.R
import github.com.rhacco.dotascoop.api.LeaderboardsResponse
import github.com.rhacco.dotascoop.base.BaseLifecycleFragment
import github.com.rhacco.dotascoop.lists.LeaderboardsAdapter
import github.com.rhacco.dotascoop.viewmodel.LeaderboardsViewModel
import kotlinx.android.synthetic.main.recycler_view.*

class LeaderboardsFragment : BaseLifecycleFragment<LeaderboardsViewModel>() {
    override val mViewModelClass = LeaderboardsViewModel::class.java
    private lateinit var mAdapter: LeaderboardsAdapter

    fun handleSearchQuery(query: String) = mAdapter.handleSearchQuery(query)

    fun showAllEntries() = mAdapter.showAllEntries()

    override fun onCreateView(
            inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.recycler_view, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        mAdapter = LeaderboardsAdapter(context)
        recycler_view.adapter = mAdapter
        val layoutManager = LinearLayoutManager(context)
        recycler_view.layoutManager = layoutManager
        recycler_view.addItemDecoration(
                DividerItemDecoration(recycler_view.context, layoutManager.orientation))
        val tabPosition = arguments["tab_position"]
        observeLiveData()
        when (tabPosition) {
            0 -> mViewModel.getLeaderboard("americas")
            1 -> mViewModel.getLeaderboard("europe")
            2 -> mViewModel.getLeaderboard("se_asia")
            else -> mViewModel.getLeaderboard("china")
        }
    }

    override fun observeLiveData() {
        mViewModel.mGetLeaderboard.observe(this, Observer<List<LeaderboardsResponse.Entry>> {
            it?.let { leaderboard -> mAdapter.update(leaderboard) }
        })
    }
}