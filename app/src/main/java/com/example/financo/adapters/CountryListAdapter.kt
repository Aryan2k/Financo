package com.example.financo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.financo.databinding.ItemUserLayoutBinding
import com.example.financo.model.Country


class CountryListAdapter(private val countryList: List<Country>) : RecyclerView.Adapter<CountryListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemUserLayoutBinding = ItemUserLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(countryList[position])
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    class ViewHolder(binding: ItemUserLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        private val binding: ItemUserLayoutBinding

        init {
            this.binding = binding
        }

        fun bind(country: Country?) {
            if (country != null && country.country_id.isNotEmpty()) {
                binding.countryValue.text = country.country_id
                binding.probabilityValue.text = country.probability.toString()
            }
        }
    }


}