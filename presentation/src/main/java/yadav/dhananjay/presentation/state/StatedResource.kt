package yadav.dhananjay.presentation.state


sealed class StatedResource {
    class Success<out T>(val data: T) : StatedResource()
    class Error(val message: String?) : StatedResource()
    class JustLoadig() : StatedResource()
}
