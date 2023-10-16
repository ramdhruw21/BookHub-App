package com.dhruw.bookhub.activity

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.room.Entity
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dhruw.bookhub.R
import com.dhruw.bookhub.database.BookDatabase
import com.dhruw.bookhub.database.BookEntity
import com.dhruw.bookhub.model.Book
import com.dhruw.bookhub.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {


    lateinit var txtBookName: TextView
    lateinit var txBookAuthor: TextView
    lateinit var txtBookPrice: TextView
    lateinit var txtBookRating: TextView
    lateinit var txtBookDesc : TextView
    lateinit var imgbookImage: ImageView
    lateinit var btnAddToFav: Button
    lateinit var ProgressBar: ProgressBar
    lateinit var ProgressLayout: RelativeLayout

    lateinit var  toolbar : androidx.appcompat.widget.Toolbar

    var bookId : String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)


        txtBookName = findViewById(R.id.textBookName)
        txBookAuthor = findViewById(R.id.textBookAuthor)
        txtBookPrice = findViewById(R.id.textBookPrice)
        txtBookRating = findViewById(R.id.textBookRating)
        imgbookImage = findViewById(R.id.imgBookImage)
        btnAddToFav = findViewById(R.id.btn)
        txtBookDesc = findViewById(R.id.textBookDesc)
        ProgressBar = findViewById(R.id.progressBar)
        ProgressLayout = findViewById(R.id.progressLayout)
        ProgressBar.visibility = View.VISIBLE
        ProgressLayout.visibility = View.VISIBLE

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "BookDetails"




        if(intent != null){

                bookId = intent.getStringExtra("book_id")

        }else{
             finish()
             Toast.makeText(this@DescriptionActivity ,"Some unexpected error occurred!", Toast.LENGTH_SHORT).show()
        }
        if(bookId == "100"){

             finish()
             Toast.makeText(this@DescriptionActivity, "Some unexpected error occurred!" ,Toast.LENGTH_SHORT).show()

        }

        //Post request

        val queue = Volley.newRequestQueue(this@DescriptionActivity)

        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)


        val jsonRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
            Response.Listener {

                            try{

                                    val success  = it.getBoolean("success")
                                    if(success){

                                          val bookJsonObject  = it.getJSONObject("book_data")
                                           ProgressLayout.visibility = View.GONE
                                         // progressBar.visibility = View.GONE//hide progress layout



                                         val bookImageUrl = bookJsonObject.getString("image")
                                         Picasso.get().load(bookJsonObject.getString("image")).error(R.drawable.default_book_cover).into(imgbookImage)
                                         txtBookName.text = bookJsonObject.getString("name")
                                         txBookAuthor.text  = bookJsonObject.getString("author")
                                         txtBookPrice.text = bookJsonObject.getString("price")
                                         txtBookRating.text = bookJsonObject.getString("rating")
                                         txtBookDesc.text = bookJsonObject.getString("description")

                                         // Check book is in favourite or not

                                            val bookEntity = BookEntity(
                                                      bookId?.toInt() as Int,
                                                      txtBookName.text.toString(),
                                                      txBookAuthor.text.toString(),
                                                      txtBookPrice.text.toString(),
                                                      txtBookRating.text.toString(),
                                                      txtBookDesc.text.toString(),
                                                      bookImageUrl
                                            )

                                           val checkFav = DBAsyncTask(applicationContext, bookEntity,1).execute()
                                           val isFav = checkFav.get()

                                          if (isFav){

                                                    btnAddToFav.text = "Remove from Favourites"
                                                    val  favColor = ContextCompat.getColor(applicationContext, R.color.colorFavourite)
                                                    btnAddToFav.setBackgroundColor(favColor)
                                          }
                                          else{

                                                  btnAddToFav.text = "Add to Favourites"
                                                  val  favColor = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
                                                  btnAddToFav.setBackgroundColor(favColor)
                                          }


                                          /**
                                           * -if the book is not a favourite, the button click will add it to
                                           * favourites.
                                           * if a book is already a favourite, the button click will remove it from
                                           * favourites */

                                        btnAddToFav.setOnClickListener{

                                                if (!DBAsyncTask(applicationContext, bookEntity, 1).execute().get()){

                                                       val async = DBAsyncTask(applicationContext, bookEntity, 2).execute()
                                                       val result = async.get()

                                                       if(result){

                                                           Toast.makeText(this@DescriptionActivity, "Book added to favourites", Toast.LENGTH_SHORT).show()
                                                           btnAddToFav.text = "Remove from Favourites"
                                                           val  favColor = ContextCompat.getColor(applicationContext, R.color.colorFavourite)
                                                           btnAddToFav.setBackgroundColor(favColor)

                                                       }
                                                       else{

                                                           Toast.makeText(this@DescriptionActivity, "Some error", Toast.LENGTH_SHORT).show()
                                                       }


                                                }else{

                                                      val async = DBAsyncTask(applicationContext, bookEntity, 3).execute()
                                                      val result = async.get()

                                                     if(result){
                                                          Toast.makeText(this@DescriptionActivity, "Book removed from favorites", Toast.LENGTH_SHORT).show()

                                                         btnAddToFav.text = "Add to favorites"
                                                         val navFavColor = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
                                                         btnAddToFav.setBackgroundColor(navFavColor)

                                                     }
                                                     else{

                                                           Toast.makeText(this@DescriptionActivity, "Some error", Toast.LENGTH_SHORT).show()
                                                     }


                                                }
                                        }

                                    }
                                   else{

                                        Toast.makeText(this@DescriptionActivity, "Some unexpected error occurred!!!", Toast.LENGTH_SHORT).show()

                                    }


                            }catch (e : Exception){

                                           Toast.makeText(this@DescriptionActivity, "Some unexpected error occurred!!!", Toast.LENGTH_SHORT).show()
                              }

        }, Response.ErrorListener {


                Toast.makeText(this@DescriptionActivity, "Volley Error $it", Toast.LENGTH_SHORT).show()

        } ){

            override fun getHeaders(): MutableMap<String, String> {

                val headers = HashMap<String, String> ()
                headers["Content-type"] = "application/json"
                headers["token"] = "8c0c99ab5818ab"

                return headers
            }
        }

          queue.add(jsonRequest)

    }


    // db setup class in background

     class  DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int) : AsyncTask<Void, Void, Boolean> (){


         val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()

         override fun doInBackground(vararg p0: Void?): Boolean {
             /**
              *  Operations to be performed in the AsyncTask class
              *  Node->3 check the database to see if a book has been added to favourites or not
              *  Node->2 Save the book in the  database as favourites.
              *  Node ->1 Remove a book from the favourites
              *  .*/
             when (mode){

                  1 -> {
                         // check DB if the book is favorite or not
                         val book = db.bookDao().getBookById(bookEntity.book_id.toString())
                         db.close()
                            return book != null //false
                   }
                  2 -> {
                         //Save the book into DB as favourite
                         db.bookDao().insertBook(bookEntity)
                         db.close()
                         return true
                 }
                 3 -> {
                      // Remove the favourite book

                     db.bookDao().deleteBook(bookEntity)
                     db.close()
                     return true
                 }

             }
               return false
         }

     }

}