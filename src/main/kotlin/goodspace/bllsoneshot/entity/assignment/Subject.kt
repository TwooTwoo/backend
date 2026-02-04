package goodspace.bllsoneshot.entity.assignment

enum class Subject {
    KOREAN, ENGLISH, MATH, RESOURCE;

    companion object {
        fun entriesExcludeResource(): List<Subject> {
            return entries.filter { it != RESOURCE }
        }
    }
}
