package exceptions

sealed class DiaryException(message: String) : Exception(message)
class EntryNotFoundException(message: String) : DiaryException(message)
class InvalidEntryException(message: String) : DiaryException(message)
class InvalidDateException(message: String) : DiaryException(message)