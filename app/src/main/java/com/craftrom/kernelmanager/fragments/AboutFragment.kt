package com.craftrom.kernelmanager.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.craftrom.kernelmanager.R
import com.craftrom.kernelmanager.activities.Choice
import com.craftrom.kernelmanager.activities.FAQsActivity
import com.craftrom.kernelmanager.activities.MainActivity
import com.craftrom.kernelmanager.activities.OSLActivity

class AboutFragment : Fragment(), View.OnClickListener {
    private lateinit var faqs: LinearLayout
    private lateinit var osl: LinearLayout
    private lateinit var imageView: ImageView
    private lateinit var ghimg: ImageView
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?  {

        Choice.choice = 3
        val sharedPreferences = context!!.getSharedPreferences("update", Context.MODE_PRIVATE)
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        faqs = view.findViewById(R.id.ll_faqs)
        osl = view.findViewById(R.id.ll_osl)
        faqs.setOnClickListener(this)
        osl.setOnClickListener(this)
        imageView = view.findViewById(R.id.tg_link)
        imageView.setOnClickListener(this)
        ghimg = view.findViewById(R.id.gh_link)
        ghimg.setOnClickListener(this)

        (activity as MainActivity).fab.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        return view
    }

    fun openTG() {
        val uri = Uri.parse("https://t.me/craft_rom")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        //intent.setPackage("org.thunderdog.challegram");
        startActivity(intent)
    }

    fun openGH() {
        val uri = Uri.parse("https://github.com/CraftRom")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    override fun onClick(v: View) {
        val view = v.id
        when (view) {
            R.id.tg_link -> openTG()
            R.id.gh_link -> openGH()
            R.id.ll_faqs -> startActivity(Intent(context, FAQsActivity::class.java))
            R.id.ll_osl -> startActivity(Intent(context, OSLActivity::class.java))
        }
    }
}