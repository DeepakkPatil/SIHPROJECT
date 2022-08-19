package com.aaks32173.sih2022new

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aaks32173.sih2022new.databinding.ActivityTodoBinding
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.*
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*

class todo  : AppCompatActivity() {

    private lateinit var binding: ActivityTodoBinding
    private lateinit var dbref : DatabaseReference
    private lateinit var dbref1 : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var rewrd : TextView
    private lateinit var userArrayList : ArrayList<usertodo>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val email = FirebaseAuth.getInstance().currentUser?.email
        val encodedemmail=encodeUserEmail(email.toString())

        rewrd=findViewById(R.id.reward)
        userRecyclerview = findViewById(R.id.userList)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)

        userArrayList = arrayListOf<usertodo>()

        setreward(encodedemmail.toString())
        getUserData()



    }

    private fun getUserData() {

       val email = intent.getStringExtra("email").toString()
        Toast.makeText(applicationContext, "Submitted successfully", Toast.LENGTH_SHORT).show()

        val today= LocalDate.now()
        dbref = FirebaseDatabase.getInstance().getReference("UserInfo/"+email+"/TODO/"+today)

        dbref.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

               if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){


                        val user = userSnapshot.getValue(usertodo::class.java)
                        userArrayList.add(user!!)


                    }


                    userRecyclerview.adapter = todoadapter(userArrayList)



                }

            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }
    private fun setreward(email :String) {

        val reference1 =FirebaseDatabase.getInstance().reference.child("UserInfo").child(email)

        reference1.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val rew= dataSnapshot.child("rewards").getValue().toString();
                Toast.makeText(applicationContext, rew+"", Toast.LENGTH_SHORT).show()

                rewrd.setText("Rewards : "+rew+" \uD83C\uDF1F")
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }
    private fun encodeUserEmail(email: String): String? {
        return email.replace(".", ",")
    }
}