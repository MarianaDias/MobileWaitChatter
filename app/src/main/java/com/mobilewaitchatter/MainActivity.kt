package com.mobilewaitchatter

import android.annotation.SuppressLint
import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.mobilewaitchatter.fragment.MyAccountFragment
import com.mobilewaitchatter.fragment.PeopleFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(PeopleFragment())

        navigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.navigation_contatos -> {
                    replaceFragment(PeopleFragment())
                    true
                }
                R.id.navigation_perfil -> {
                    replaceFragment(MyAccountFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: android.support.v4.app.Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_layout, fragment)
                .commit()
    }
}
