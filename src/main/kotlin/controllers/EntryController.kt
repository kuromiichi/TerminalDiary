package controllers

import exceptions.*
import models.Entry
import repositories.EntryRepository
import repositories.EntryRepositoryImpl
import java.time.LocalDate

class EntryController private constructor(private val repository: EntryRepository) {

    companion object {
        private var instance: EntryController? = null

        fun getInstance(repository: EntryRepository? = null): EntryController {
            if (instance == null)
                instance = EntryController(repository ?: EntryRepositoryImpl())
            return instance as EntryController
        }
    }

    fun count(): Int {
        return repository.count()
    }

    fun createEntry(entry: Entry): Entry {
        val check = checkValidEntry(entry)
        if (!check.first) throw InvalidEntryException("Invalid entry (${check.second})")
        return repository.createEntry(entry)
    }

    fun deleteEntry(entry: Entry): Entry {
        val check = checkValidEntry(entry)
        if (!check.first) throw InvalidEntryException("Invalid entry (${check.second})")
        return repository.deleteEntry(entry)
            ?: throw EntryNotFoundException("Entry not found")
    }

    fun modifyEntry(entry: Entry, newEntry: Entry): Entry {
        var check = checkValidEntry(entry)
        if (!check.first) throw InvalidEntryException("Invalid old entry (${check.second})")
        check = checkValidEntry(newEntry)
        if (!check.first) throw InvalidEntryException("Invalid new entry (${check.second})")
        return repository.modifyEntry(entry, newEntry)
            ?: throw EntryNotFoundException("Entry not found")
    }

    fun findEntryByName(name: String): List<Entry> {
        return repository.findEntryByName(name)
    }

    fun findEntryByDate(date: LocalDate): List<Entry> {
        if (!checkValidDate(date)) throw InvalidDateException("Invalid date")
        return repository.findEntryByDate(date)
    }

    fun findAllEntries(): List<Entry> {
        return repository.findAllEntries()
    }

    fun exportToFile() {
        repository.exportToFile()
    }

    fun importFromFile() {
        repository.importFromFile()
    }

    private fun checkValidEntry(entry: Entry): Pair<Boolean, String> {
        return when {
            !checkValidDate(entry.date) -> false to "Invalid date"
            entry.title.isEmpty() -> false to "Empty title"
            entry.content.isEmpty() -> false to "Empty content"
            else -> true to ""
        }
    }

    private fun checkValidDate(date: LocalDate): Boolean {
        return (date <= LocalDate.now())
    }

}