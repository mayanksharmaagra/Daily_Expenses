package com.jrProfessor.todoapp.repository

import com.jrProfessor.todoapp.roomdb.PassbookDao
import com.jrProfessor.todoapp.roomdb.PassbookDto

interface DatabaseRepository {
    suspend fun insertData(passbookDto: PassbookDto)
}