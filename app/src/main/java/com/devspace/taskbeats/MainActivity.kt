package com.devspace.taskbeats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            TaskBeatDataBase::class.java, "database-task-beat"
        ).build()
    }

    private val categoryDao: CategoryDao by lazy {
        db.getCategoryDao()
    }

    private val taskDao: TaskDao by lazy {
        db.getTaskDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        insertDefaultCategory()
        insertDefaultTask()

        val rvCategory = findViewById<RecyclerView>(R.id.rv_categories)
        val rvTask = findViewById<RecyclerView>(R.id.rv_tasks)

        val taskAdapter = TaskListAdapter()
        val categoryAdapter = CategoryListAdapter()

        categoryAdapter.setOnClickListener { selected ->
            val categoryTemp = categories.map { item ->
                when {
                    item.name == selected.name && !item.isSelected -> item.copy(isSelected = true)
                    item.name == selected.name && item.isSelected -> item.copy(isSelected = false)
                    else -> item
                }
            }

            val taskTemp =
                if (selected.name != "ALL") {
                    tasks.filter { it.category == selected.name }
                } else {
                    tasks
                }
            taskAdapter.submitList(taskTemp)

            categoryAdapter.submitList(categoryTemp)
        }

        rvCategory.adapter = categoryAdapter
        getCategoriesFromDataBase(categoryAdapter)

        rvTask.adapter = taskAdapter
        taskAdapter.submitList(tasks)
    }

    private fun insertDefaultCategory() {
        // o map converte o UiData(CategoryList Mock) em category entity
        val categoriesEntity = categories.map {
            CategoryEntity(
                name = it.name,
                isSelected = it.isSelected
            )
        }
        GlobalScope.launch(Dispatchers.IO) {
            //TODO: Coroutines auxiliary o loading em background dos dados da tela.
            categoryDao.insetAll(categoriesEntity)
        }
    }


    private fun insertDefaultTask() {
        // o map converte o UiData(CategoryList Mock) em category entity
        val tasksEntity = tasks.map {
            TaskEntity(
                name = it.name,
                category = it.category
            )
        }
        GlobalScope.launch(Dispatchers.IO) { // roda em background por nao rodar na main thread
            //TODO: Coroutines auxiliary o loading em background dos dados da tela.
            taskDao.insetAll(tasksEntity)
        }
    }

    private fun getCategoriesFromDataBase(categoryListAdapter: CategoryListAdapter) {
        GlobalScope.launch(Dispatchers.IO) {
            // Aqui você busca do DAO uma lista de entidades (CategoryEntity), que é o modelo da camada de dados (representa a tabela no banco).
            val categoriesFromdb: List<CategoryEntity> = categoryDao.getAll()
            // Aqui você transforma (map) cada CategoryEntity em um CategoryUiData, que é o modelo da camada de apresentação/UI
            val categoriesUiData = categoriesFromdb.map {
                CategoryUiData(
                    name = it.name,
                    isSelected = it.isSelected
                )
            }
            //Aqui você envia essa lista já convertida para o Adapter (que vai exibir na tela).
            categoryListAdapter.submitList(categoriesUiData)
        }
    }

}

//val categories: List<CategoryUiData> = listOf()
// Inserir as tasks na base de dados

// val tasks: List<TaskUiData> = listOf()