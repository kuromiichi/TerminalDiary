package repositories

import models.Entry
import java.io.*
import java.time.LocalDate

class EntryRepositoryImpl : EntryRepository {
    private var repository = mutableListOf<Entry>()

    override fun count(): Int {
        return repository.size
    }

    override fun createEntry(entry: Entry): Entry {
        repository.add(entry)
        return entry
    }

    override fun deleteEntry(entry: Entry): Entry? {
        return if (repository.remove(entry)) entry else null
    }

    override fun modifyEntry(entry: Entry, newEntry: Entry): Entry? {
        return if (repository.remove(entry)) {
            repository.add(newEntry)
            newEntry
        } else null
    }

    override fun findEntryByName(name: String): List<Entry> {
        return repository
            .filter { it.title.contains(name, true) }
    }

    override fun findEntryByDate(date: LocalDate): List<Entry> {
        return repository
            .filter { it.date == date }
    }

    override fun findAllEntries(): List<Entry> {
        return repository.toList()
    }

    override fun exportToFile() {
        val file = "diary.txt"
        try {
            val oos = ObjectOutputStream(FileOutputStream(file))
            oos.writeObject(repository)
            oos.close()
        } catch (e: IOException) {
            println("Error occurred while exporting to file")
        }
    }

    override fun importFromFile() {
        val file = "diary.txt"
        if (File(file).exists()) {
            try {
                val ois = ObjectInputStream(FileInputStream(file))
                repository = ois.readObject() as MutableList<Entry>
                ois.close()
            } catch (e: IOException) {
                println("Error occurred while importing from file")
            }
        }
    }
}