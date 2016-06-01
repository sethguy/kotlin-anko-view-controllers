package com.ivieleague.kotlin.anko.viewcontrollers.containers

import com.ivieleague.kotlin.anko.animation.AnimationSet
import com.ivieleague.kotlin.anko.viewcontrollers.ViewController
import com.ivieleague.kotlin.runAll
import java.util.*

/**
 * Used to create left/right tabs.
 * @param startIndex The first view controller's index to show.
 * @param vcs The view controllers to display.
 * Created by jivie on 10/14/15.
 */
class VCTabs(startIndex: Int, vcs: List<ViewController>) : VCContainerImpl() {

    constructor(startIndex: Int, vararg vcs: ViewController) : this(startIndex, vcs.toList())

    val viewControllers: Array<ViewController> = Array(vcs.size, { vcs[it] })
    val onIndexChange = ArrayList<(Int) -> Unit>()
    var index: Int = startIndex
        set(value) {
            field = value
            onIndexChange.runAll(value)
        }

    override val current: ViewController get() = viewControllers[index]

    fun swap(newIndex: Int) {
        if (index == newIndex) return;
        index = newIndex
    }

    var oldIndex: Int = startIndex
    val onChangeListener: (Int) -> Unit = { it: Int ->
        swapListener?.invoke(viewControllers[it],
                if (it > oldIndex) {
                    AnimationSet.slidePush
                } else {
                    AnimationSet.slidePop
                },
                {})
        oldIndex = it
        onSwap.forEach { it(current) }
    }

    init {
        onIndexChange.add(onChangeListener)
    }

    override fun dispose() {
        onIndexChange.remove(onChangeListener)
        viewControllers.forEach { it.dispose() }
    }

}