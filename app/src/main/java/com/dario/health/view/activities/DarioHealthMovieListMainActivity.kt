package com.dario.health.view.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dario.health.BuildConfig
import com.dario.health.DarioHealthApplication
import com.dario.health.R
import com.dario.health.adaptors.DarioHealthMovieListAdaptor
import com.dario.health.database.MovieListEntity
import com.dario.health.databinding.DarioHealthMoviesListActivityMainBinding
import com.dario.health.networkUtils.NetworkUtils
import com.dario.health.repository.Response
import com.dario.health.viewmodel.MovieViewModel
import com.dario.health.viewmodel.MovieViewModelFactory
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import java.util.concurrent.TimeUnit


class DarioHealthMovieListMainActivity : AppCompatActivity(),
    DarioHealthMovieListAdaptor.OnMovieViewClicked,
    DarioHealthMovieListAdaptor.OnFavoritesMovieItemClicked {
    val TAG = DarioHealthMovieListMainActivity::class.java.simpleName
    lateinit var layoutBinder: DarioHealthMoviesListActivityMainBinding
    lateinit var movieViewModel: MovieViewModel
    var darioHealthMovieListAdaptor: DarioHealthMovieListAdaptor? = null
    var updatedMovieList: ArrayList<MovieListEntity>? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutBinder = DarioHealthMoviesListActivityMainBinding.inflate(layoutInflater)
        setContentView(layoutBinder.root)
        setSupportActionBar(layoutBinder.toolbarMain)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayShowHomeEnabled(false)
        layoutBinder.toolbarMain.overflowIcon?.setTint(Color.WHITE)
        // layoutBinder.searchView.setOnQueryTextListener(this)
        layoutBinder.searchView.clearFocus();
        layoutBinder.txtViewDataNotAvailable.visibility = View.VISIBLE
        layoutBinder.txtViewDataNotAvailable.setText("Search your movies")
        layoutBinder.recyclerViewManagedView?.apply {
            layoutManager =
                LinearLayoutManager(this@DarioHealthMovieListMainActivity)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
            setHasFixedSize(true)


        }
        val repository = DarioHealthApplication.darioHealthMovieRepository
        layoutBinder.floatingActionButtonRefresh.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, DarioHealthFavoritesMovieListActivity::class.java)
            startActivity(intent)
        })
        movieViewModel =
            ViewModelProvider(
                this,
                MovieViewModelFactory(repository)
            ).get(MovieViewModel::class.java)
        movieViewModel.liveDataMovieList.observe(this, Observer {
            when (it) {
                is Response.Loading -> {
                    Log.e(TAG, "Loading " + it.isloading + " " + Thread.currentThread().name)
                    if (it.isloading == true) {
                        /*  DarioHealthApplication.app?.showProgressDialog(
                              this,
                              "Loading",
                              "Please wait"
                          )*/
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
                        if (darioHealthMovieListAdaptor == null) {
                            darioHealthMovieListAdaptor =
                                DarioHealthMovieListAdaptor(updatedMovieList!!)
                            darioHealthMovieListAdaptor!!.setOnFavoritesMovieItemClicked(this@DarioHealthMovieListMainActivity)
                            darioHealthMovieListAdaptor!!.setOnMovieViewClicked(this@DarioHealthMovieListMainActivity)
                            layoutBinder.recyclerViewManagedView.adapter =
                                darioHealthMovieListAdaptor
                        } else {
                            darioHealthMovieListAdaptor!!.notifyDataSetChanged()
                        }
                        DarioHealthApplication.app?.showSnackbar(
                            this@DarioHealthMovieListMainActivity,
                            layoutBinder.root,
                            "Movie list is fetched successfully",
                            DarioHealthApplication.ColorType.SUCCESS
                        )
                    } else {
                        if (updatedMovieList == null) {
                            updatedMovieList = ArrayList<MovieListEntity>()

                        }
                        updatedMovieList!!.clear();
                        darioHealthMovieListAdaptor?.let {
                            it.notifyDataSetChanged()
                        }
                        layoutBinder.txtViewDataNotAvailable.visibility = View.VISIBLE

                        DarioHealthApplication.app?.showSnackbar(
                            this@DarioHealthMovieListMainActivity,
                            layoutBinder.root,
                            resources.getString(R.string.data_not_available),
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
                    darioHealthMovieListAdaptor?.let {
                        it.notifyDataSetChanged()
                    }
                    DarioHealthApplication.app?.showSnackbar(
                        this@DarioHealthMovieListMainActivity,
                        layoutBinder.root,
                        "" + it.errorMsg,
                        DarioHealthApplication.ColorType.ERROR
                    )
                    DarioHealthApplication.app?.dismissProgressDialog()
                }


            }

        })
        Observable
            .create(ObservableOnSubscribe<String> { subscriber ->
                layoutBinder.searchView.setOnQueryTextListener(object :
                    SearchView.OnQueryTextListener {
                    override fun onQueryTextChange(newText: String?): Boolean {
                        subscriber.onNext(newText!!)
                        return false
                    }

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        subscriber.onNext(query!!)
                        layoutBinder.txtViewDataNotAvailable.visibility = View.GONE
                        return false
                    }
                })
            })
            .map { text -> text.trim() }
            .debounce(300, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .filter { text -> text.isNotBlank() }
            .subscribe { text ->
                Log.d(TAG, "subscriber: $text")
                if (NetworkUtils.isInternetAvailable(this)) {
                    if (BuildConfig.IS_LOG_ENABLED)
                        Log.e(TAG, "Observable")
                    movieViewModel.getMovieList(text)
                } else {
                    DarioHealthApplication.app?.showSnackbar(
                        this@DarioHealthMovieListMainActivity,
                        layoutBinder.root,
                        resources.getString(R.string.no_internet_connection),
                        DarioHealthApplication.ColorType.ERROR
                    )
                }
            }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.menu_main_favorites -> {
                val intent = Intent(this, DarioHealthFavoritesMovieListActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }


    }

    override fun onFavoritesMovieItemClicked(position: Int) {
        Log.e("MovieItemClicked", "" + position);
        movieViewModel.setFavoritesMovieStatus(position)

    }

    override fun onMovieViewClicked(position: Int) {
        val intent = Intent(this, DarioHealthMovieListDetailActivity::class.java)
        val mGson = Gson()
        val value: String = mGson.toJson(updatedMovieList?.get(position))
        intent.putExtra("movieDetail", value)
        startActivity(intent)

    }


}

