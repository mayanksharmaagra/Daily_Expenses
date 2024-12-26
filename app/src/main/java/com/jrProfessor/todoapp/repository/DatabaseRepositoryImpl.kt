package com.jrProfessor.todoapp.repository

import com.jrProfessor.todoapp.roomdb.DatabaseBuilder
import com.jrProfessor.todoapp.roomdb.PassbookDao
import com.jrProfessor.todoapp.roomdb.PassbookDto
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(private val passbookDao: PassbookDao) :
    DatabaseRepository {
    override suspend fun insertData(passbookDto: PassbookDto) {
        passbookDao.insertEntry(passbookDto)
    }
}