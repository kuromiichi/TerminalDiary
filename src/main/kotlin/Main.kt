import controllers.EntryController
import models.Entry
import java.lang.Runtime.getRuntime
import java.time.LocalDate
import java.time.format.DateTimeParseException

fun main() {
    EntryController.getInstance().also { it.importFromFile() }
    getRuntime().addShutdownHook(Thread {
        println("\nSaving diary")
        EntryController.getInstance().exportToFile()
        println("Done - Exiting program")
    })
    mainMenu()
}

fun mainMenu() {
    var message: String? = null
    var op = 0
    do {
        clear()
        println("==============")
        println("Terminal Diary")
        println("==============")
        println("Main Menu")
        println("--------------")
        println(message ?: "")
        message = null
        println("\nChoose an option:")
        println("\t1. Create diary entry")
        println("\t2. Delete diary entry")
        println("\t3. Modify diary entry")
        println("\t4. Find entry")
        println("\t5. List all entries")
        println("\n\t0. Exit")
        print("\n> ")
        op = readln().toIntOrNull() ?: -1
        clear()
        when (op) {
            1 -> createEntry()
            2 -> deleteEntry()
            3 -> modifyEntry()
            4 -> findEntry()
            5 -> listAllEntries()
            0 -> print("")
            else -> message = "Invalid option"
        }
    } while (op != 0)
}

fun createEntry() {
    val date = getDate()
    val title = getTitle()
    val content = getContent()
    val emotion = getEmotion()
    println("New entry will look like this:")
    println("$date - [${emotion.name}] - $title\n")
    println(content)
    if (confirm()) EntryController
        .getInstance()
        .createEntry(Entry(date, title, content, emotion))
}

fun deleteEntry() {}

fun modifyEntry() {}

fun findEntry() {
    var op = 0
    do {
        println("Find entry by...")
        println("\t1. Name")
        println("\t2. Date")
        println("\n\t0. Cancel")
        println("\n> ")
        op = readln().toIntOrNull() ?: -1
        when (op) {
            1 -> findByName()
            2 -> findByDate()
            0 -> print("")
            else -> println("Invalid option")
        }
    } while (op != 0)
}

private fun findByName() {}

private fun findByDate() {}

fun listAllEntries() {
    EntryController.getInstance().findAllEntries().also { println(it) }
}

private fun confirm(): Boolean {
    var op = ""
    val validAnswers = listOf("y", "Y", "yes", "YES", "n", "N", "no", "NO")
    do {
        println("Proceed? (y/n)")
        print("> ")
        op = readln()
        if (op !in validAnswers) println("Invalid option")
    } while (op !in validAnswers)
    return op.contains("y")
}

private fun getDate(curdate: Boolean = true): LocalDate {
    if (curdate) {
        println("Proceed to use current date")
        if (confirm()) return LocalDate.now()
    }
    val dateRegex = """\d{4}-\d{2}-\d{2}""".toRegex()
    var valid = false
    var date = ""
    var parsedDate = LocalDate.now()
    do {
        do {
            println("Enter a date (YYYY-MM-DD)")
            print("> ")
            date = readln()
            if (!date.matches(dateRegex)) println("Invalid date: incorrect format")
        } while (!date.matches(dateRegex))
        try {
            parsedDate = LocalDate.parse(date)
        } catch (e: DateTimeParseException) { println("Invalid date: that date does not exist") }
        valid = parsedDate <= LocalDate.now()
        if (!valid) println("Invalid date: cannot create entries in the future")
    } while (!valid)
    return parsedDate
}

private fun getTitle(): String {
    var title = ""
    do {
        println("Enter a title:")
        print("> ")
        title = readln()
        if (title.isEmpty()) println("Invalid title: cannot be empty")
    } while (title.isEmpty())
    return title
}

private fun getContent(): String {
    var content = ""
    do {
        println("Enter a content:")
        print("> ")
        content = readln()
        if (content.isEmpty()) println("Invalid content: cannot be empty")
    } while (content.isEmpty())
    return content
}

private fun getEmotion(): Entry.Emotion {
    var set = false
    var em = ""
    do {
        println("Enter an emotion (depressed, sad, neutral, happy, very happy):")
        print("> ")
        em = readln()
        set = when (em) {
            in listOf("depressed", " sad", " neutral", "happy", "very happy") -> true
            else -> false
        }
        if (!set) println("Invalid emotion")
    } while (!set)
    return when (em) {
        "depressed" -> Entry.Emotion.DEPRESSED
        "sad" -> Entry.Emotion.SAD
        "happy" -> Entry.Emotion.HAPPY
        "very happy" -> Entry.Emotion.VERY_HAPPY
        else -> Entry.Emotion.NEUTRAL
    }
}

private fun clear() {
    repeat(30) { println() }
}
