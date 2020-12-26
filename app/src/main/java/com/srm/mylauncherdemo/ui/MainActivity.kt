package com.srm.mylauncherdemo.ui

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srm.launchersdk.model.AppDetails
import com.srm.launchersdk.MyLauncherManager
import com.srm.mylauncherdemo.R
import com.srm.mylauncherdemo.interfaces.OnItemClickListener
import com.srm.mylauncherdemo.adapter.RecyclerViewAdapter

class MainActivity : AppCompatActivity(), OnItemClickListener {

    //private lateinit var myLauncher: MyLauncherManager
    private lateinit var appList: MutableList<AppDetails>
    private lateinit var appListAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appList = MyLauncherManager.getInstalledApps(this)

        initSearchView()
        loadList()
    }

    private fun loadList() {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewLauncherList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        //recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
        appListAdapter = RecyclerViewAdapter(appList, this)
        recyclerView.adapter = appListAdapter
    }

    override fun onItemClicked(appDetails: AppDetails) {
        appDetails.let { details ->
            Toast.makeText(this, "Launching ${details.appName}", Toast.LENGTH_SHORT).show()
            if (details.launchIntent != null) {
                startActivity(details.launchIntent)
            }
        }
    }

    private fun initSearchView() {
        val searchView: SearchView = findViewById(R.id.app_search)
        val searchIcon = searchView.findViewById<ImageView>(R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.parseColor("#FF6200EE"))

        val cancelIcon = searchView.findViewById<ImageView>(R.id.search_close_btn)
        cancelIcon.setColorFilter(Color.parseColor("#FF6200EE"))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                appListAdapter.filter.filter(newText)
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_ascending -> {
                try {
                    MyLauncherManager.sortListByAscending()
                    appListAdapter.notifyDataSetChanged()
                    Toast.makeText(
                        applicationContext,
                        resources.getString(R.string.text_sort_asc),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) { e.printStackTrace() }
                true
            }
            R.id.action_sort_descending -> {
                try {
                    MyLauncherManager.sortListByDescending()
                    appListAdapter.notifyDataSetChanged()
                    Toast.makeText(
                        applicationContext,
                        resources.getString(R.string.text_sort_dsc),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) { e.printStackTrace() }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}