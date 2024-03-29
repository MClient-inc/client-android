package ru.mclient.ui.view.tags.core.base

/**
 * [MarkupParser] is a protocol of a markup parser.
 */

interface MarkupParser<N : BaseNode<N>, R : BaseRule<N>> {

    fun parse(source: String, rules: List<R>): Collection<N>?

}