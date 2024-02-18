package ski.mashiro.util

import java.util.concurrent.atomic.AtomicReference

/**
 * @author mashirot
 * 2024/2/17 17:08
 */
class ObservableAtomicReference<T>(
    initValue: T,
    private val setFunc: (AtomicReference<T>, newValue: T, (T) -> Unit) -> Unit
) {
    private val atomicReference = AtomicReference(initValue)
    private val listenerList = mutableListOf<(T) -> Unit>()

    var value: T
        get() = atomicReference.get()
        set(value) = setFunc.invoke(atomicReference, value, this::invokeListener)

    fun addListener(listener: (T) -> Unit) = listenerList.add(listener)

    fun removeListener(listener: (T) -> Unit) = listenerList.remove(listener)

    private fun invokeListener(value: T) = listenerList.forEach { it.invoke(value) }
}