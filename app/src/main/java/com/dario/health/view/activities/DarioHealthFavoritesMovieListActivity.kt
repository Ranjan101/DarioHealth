package com.dario.health.view.activities

import android.R
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cheezycode.randomquote.api.DarioHealthServiceAPI
import com.cheezycode.randomquote.api.RetrofitHelper
import com.dario.health.DarioHealthApplication
import com.dario.health.adaptors.DarioHealthFavoritesMovieListAdaptor
import com.dario.health.adaptors.DarioHealthMovieListAdaptor
import com.dario.health.database.FavoritesMovieListDatabase
import com.dario.health.database.MovieListEntity
import com.dario.health.databinding.DarioHealthFavoritesMovieListActivityBinding
import com.dario.health.repository.DarioHealthMovieRepository
import com.dario.health.repository.Response
import com.dario.health.viewmodel.FavoritesMovieViewModel
import com.dario.health.viewmodel.FavoritesMovieViewModelFactory

class DarioHealthFavoritesMovieListActivity : AppCompatActivity(),
    DarioHealthFavoritesMovieListAdaptor.OnFavoritesMovieItemClicked {
    val TAG = DarioHealthFavoritesMovieListActivity::class.java.simpleName
    lateinit var layoutBinder: DarioHealthFavoritesMovieListActivityBinding

    lateinit var movieViewModel: FavoritesMovieViewModel
    var darioHealthFavoritesMovieListAdaptor: DarioHealthFavoritesMovieListAdaptor? = null
    var updatedMovieList: ArrayList<MovieListEntity>? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutBinder = DarioHealthFavoritesMovieListActivityBinding.inflate(layoutInflater)
        setContentView(layoutBinder.root)
        setSupportActionBar(layoutBinder.toolbarMain)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.elevation = 0f
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        layoutBinder.recyclerViewManagedView?.apply {
            layoutManager =
                LinearLayoutManager(this@DarioHealthFavoritesMovieListActivity)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
            setHasFixedSize(true)


        }
        val darioHealthServiceAPI =
            RetrofitHelper.getInstance().create(DarioHealthServiceAPI::class.java)
        val database = FavoritesMovieListDatabase.getDatabase(this)
        val repository =
            DarioHealthMovieRepository(darioHealthServiceAPI, database, this)
        movieViewModel =
            ViewModelProvider(
                this,
                FavoritesMovieViewModelFactory(repository)
            ).get(FavoritesMovieViewModel::class.java)

        movieViewModel.liveDataMovieList.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                    Log.e(TAG, "Loading " + it.isloading + " " + Thread.currentThread().name)
                    if (it.isloading == true) {
                        DarioHealthApplication.app?.showProgressDialog(
                            this,
                            "Loading",
                            "Please wait"
                        )
                    } else {
                        DarioHealthApplication.app?.dismissProgressDialog()
                    }
                }
                is Response.Success -> {
                    if (updatedMovieList == null) {
                        updatedMovieList = ArrayList<MovieListEntity>()

                    }
                    updatedMovieList!!.clear();


                    Log.e(TAG, "Success " + it.data?.size)
                    if (it.data != null && !it.data.isEmpty()) {
                        layoutBinder.txtViewDataNotAvailable.visibility = View.GONE
                        updatedMovieList!!.addAll(it.data)
                        Log.e(TAG, "Size " + it.data)
                        if (darioHealthFavoritesMovieListAdaptor == null) {
                            darioHealthFavoritesMovieListAdaptor =
                                DarioHealthFavoritesMovieListAdaptor(updatedMovieList!!)
                            darioHealthFavoritesMovieListAdaptor!!.setOnFavoritesMovieItemClicked(
                                this@DarioHealthFavoritesMovieListActivity
                            )
                            // darioHealthMovieListAdaptor!!.setOnMovieViewClicked(this@DarioHealthFavoritesMovieListActivity)
                            layoutBinder.recyclerViewManagedView.adapter =
                                darioHealthFavoritesMovieListAdaptor
                        } else {
                            darioHealthFavoritesMovieListAdaptor!!.notifyDataSetChanged()
                        }
                        DarioHealthApplication.app?.showSnackbar(
                            this@DarioHealthFavoritesMovieListActivity,
                            layoutBinder.root,
                            "Movie list is fetched successfully",
                            DarioHealthApplication.ColorType.SUCCESS
                        )
                    } else {
                        if (updatedMovieList == null) {
                            updatedMovieList = ArrayList<MovieListEntity>()

                        }
                        updatedMovieList!!.clear();
                        darioHealthFavoritesMovieListAdaptor?.let {
                            it.notifyDataSetChanged()
                        }
                        layoutBinder.txtViewDataNotAvailable.visibility = View.VISIBLE

                        DarioHealthApplication.app?.showSnackbar(
                            this@DarioHealthFavoritesMovieListActivity,
                            layoutBinder.root,
                            resources.getString(com.dario.health.R.string.data_not_available),
                            DarioHealthApplication.ColorType.ERROR
                        )
                        layoutBinder.txtViewDataNotAvailable.visibility = View.VISIBLE


                    }

                    DarioHealthApplication.app?.dismissProgressDialog()


                }
                is Response.Error -> {
                    Log.e(TAG, "Error" + it.errorMsg)
                    if (updatedMovieList == null) {
                        updatedMovieList = ArrayList<MovieListEntity>()

                    }
                    updatedMovieList!!.clear();
                    layoutBinder.txtViewDataNotAvailable.visibility = View.VISIBLE
                    darioHealthFavoritesMovieListAdaptor?.let {
                        it.notifyDataSetChanged()
                    }
                    DarioHealthApplication.app?.showSnackbar(
                        this@DarioHealthFavoritesMovieListActivity,
                        layoutBinder.root,
                        "" + it.errorMsg,
                        DarioHealthApplication.ColorType.ERROR
                    )
                    DarioHealthApplication.app?.dismissProgressDialog()
                }


            }

        })
        movieViewModel.getAllFavoritesMovieList();
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onFavoritesMovieItemClicked(position: Int) {
        movieViewModel.setFavoritesMovieStatus(position)
    }
}