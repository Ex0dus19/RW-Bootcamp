package com.minneydev.pokedex.repository

import com.minneydev.pokedex.model.pokemon.ApiPokemon
import com.minneydev.pokedex.model.pokemon.Pokemon
import com.minneydev.pokedex.networking.PokemonApi
import com.minneydev.pokedex.savePokemonData.PokemonDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Does all the talking between the [PokemonApi] and the [PokemonDatabase]
 */

class PokemonRepository(private val pokemonApi: PokemonApi,
                        private val pokemonDb: PokemonDatabase) {

    fun savePokemon(pokemon: ApiPokemon?) : Pokemon? {
        val tempPokemon = pokemon?.mapToPokemon()
        if (tempPokemon != null) {
            CoroutineScope(Dispatchers.IO).launch {
                pokemonDb.pokemonDao().insertPokemon(tempPokemon)
            }
        }
        return tempPokemon
    }

    fun nukeDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            pokemonDb.pokemonDao().nukeTable()
        }
    }

    suspend fun fetchPokemonById(id: String) : ApiPokemon? {
        return pokemonApi.fetchPokemonById(id)
    }

    suspend fun fetchAllPokemon() : List<Pokemon> {
        return pokemonDb.pokemonDao().getAllPokemon()
    }




}