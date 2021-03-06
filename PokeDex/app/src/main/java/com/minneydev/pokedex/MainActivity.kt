package com.minneydev.pokedex

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.minneydev.pokedex.model.pokemon.Pokemon
import com.minneydev.pokedex.ui.PokemonAdapter
import com.minneydev.pokedex.viewModel.PokemonViewModel
import com.minneydev.pokedex.viewModel.PokemonViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.core.KoinComponent
import org.koin.core.get

class MainActivity : AppCompatActivity(), KoinComponent {

    companion object {
        private lateinit var adapter: PokemonAdapter
        //The Adapter is in the CompObj just so the setPokemon() will work
        fun setPokemon(pokemon: Pokemon) { adapter.setPokemon(pokemon) }
        fun clearRecyclerView() { adapter.clear() }
    }

    private lateinit var pokemonViewModel: PokemonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = PokemonAdapter {
            showDetailActivity(it)
        }
        pokemonViewModel = ViewModelProviders.of(this,
            PokemonViewModelFactory(get(), get(), get())
        )
            .get(PokemonViewModel::class.java)
        pokemonRecyclerView.layoutManager = LinearLayoutManager(this)
        observePokemon()
        pokemonRecyclerView.adapter = adapter
    }

    /**
    Observes Pokemon List in [PokemonViewModel]
     */
    private fun observePokemon() {
        pokemonViewModel.fetchPokemonList().let {
            it.observe(this, Observer { pokemonList ->
                putOnRecyclerView(pokemonList)
            })
        }

    }

    /**
    Code to change [R.id.pokemonRecyclerView]
     */
    private fun putOnRecyclerView(pokemon: List<Pokemon>) {
        pokemon.forEach {
            adapter.setPokemon(it)
        }
    }

    /**
     * Code to change Pokemon Generation
     */
    private fun showGeneration(gen: Int) {
        clearRecyclerView()
        pokemonViewModel.changeGeneration(gen)
        generationChangeToast(gen)
    }

    private fun generationChangeToast(gen: Int) {
        Toast.makeText(applicationContext,getString(R.string.gen_change_toast, gen.toString()),Toast.LENGTH_SHORT).show()
    }

    /**
     * Code for [R.menu.menu]
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.aboutMenu -> showAbout()
            R.id.genOne -> showGeneration(1)
            R.id.genTwo -> showGeneration(2)
            R.id.genThree -> showGeneration(3)
            R.id.genFour -> showGeneration(4)
        }
        return true
    }

    private fun showAbout() {
        AlertDialog.Builder(this)
                .setTitle(R.string.about_title)
                .setMessage(R.string.about_message)
                .create().show()
    }

    private fun showDetailActivity(pokemon: Pokemon) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("POKEMON", pokemon)
        startActivity(intent)
        animateTransition()
    }

    private fun animateTransition() {
        overridePendingTransition(R.anim.slide_in_left,
            R.anim.slide_out_right);
    }


}