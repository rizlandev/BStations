package com.rizlan_dev.bstations.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.rizlan_dev.bstations.R
import com.rizlan_dev.bstations.model.Stations


class CustomListAdapter (context: Context, items: ArrayList<Stations>) : BaseAdapter() {
        private val context: Context
        private val items: ArrayList<Stations>

        override fun getCount(): Int {
            return items.size //returns total of items in the list
        }

        override fun getItem(position: Int): Any {
            return items[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?):
                View? {
            var convertView: View? = convertView
            if (convertView == null) {
                convertView = LayoutInflater.from(context)
                    .inflate(R.layout.station_listview_item, parent, false)
            }
            val currentItem = getItem(position) as Stations
            val textView_location = convertView
                ?.findViewById(R.id.tv_id_name) as TextView

            val textView_distance = convertView
                ?.findViewById(R.id.tv_dis) as TextView

            val textView_avb_bike = convertView
                ?.findViewById(R.id.tv_avb_bike) as TextView

            val textView_avb_space = convertView
                ?.findViewById(R.id.tv_avb_place) as TextView

            textView_location.text = currentItem.id+" "+ currentItem.lable
            textView_distance.text = "800m * Bike Station"
            textView_avb_bike.text = currentItem.avb_bike
            textView_avb_space.text = currentItem.avb_place

            return convertView
        }
        init {
            this.context = context
            this.items = items
        }
    }
