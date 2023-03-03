package repositories

import models.Entry
import java.time.LocalDate

interface EntryRepository {
    fun count(): Int
    fun createEntry(entry: Entry): Entry
    fun deleteEntry(entry: Entry): Entry?
    fun modifyEntry(entry: Entry, newEntry: Entry): Entry?
    fun findEntryByName(name: String): List<Entry>
    fun findEntryByDate(date: LocalDate): List<Entry>
    fun findAllEntries(): List<Entry>
    fun exportToFile()
    fun importFromFile()
}