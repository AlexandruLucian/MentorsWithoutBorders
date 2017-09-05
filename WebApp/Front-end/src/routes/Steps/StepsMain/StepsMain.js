import React, { Component, PropTypes } from 'react'
import StepsItem from '../StepsItem/StepsItem'
import StepsHeader from '../StepsHeader/StepsHeader'
import StepsFooter from '../StepsFooter/StepsFooter'
import { SHOW_ALL, SHOW_COMPLETED, SHOW_ACTIVE } from './StepsMainModule'
import { actions } from './StepsMainModule'

const STEPS_FILTERS = {
  [SHOW_ALL]: () => true,
  [SHOW_ACTIVE]: step => !step.completed,
  [SHOW_COMPLETED]: step => step.completed
}

class StepsMain extends Component {
  static propTypes = {
    steps: PropTypes.array.isRequired
  }

  state = { filter: SHOW_ALL }

  handleClearCompleted = () => {
    this.props.actions.clearCompleted()
  }

  handleShow = filter => {
    this.setState({ filter })
  }

  renderToggleAll(completedCount) {
    const { steps } = this.props
    if (steps.length > 0) {
      return (
        <input className="toggle-all"
               type="checkbox"
               checked={completedCount === steps.length}
               onChange={actions.completeAll} />
      )
    }
  }

  renderFooter(completedCount) {
    const { steps } = this.props
    const { filter } = this.state
    const activeCount = steps.length - completedCount

    if (steps.length) {
      return (
        <StepsFooter completedCount={completedCount}
                activeCount={activeCount}
                filter={filter}
                onClearCompleted={this.handleClearCompleted}
                onShow={this.handleShow} />
      )
    }
  }

  render() {
    const { steps } = this.props
    const { filter } = this.state

    const filteredSteps = steps.filter(STEPS_FILTERS[filter])
    const completedCount = steps.reduce((count, step) =>
      step.completed ? count + 1 : count,
      0
    )

    return (
      <div>
        <StepsHeader addStep={actions.addStep} />
        <section className="main">
          {this.renderToggleAll(completedCount)}
          <ul className="step-list">
            {filteredSteps.reverse().map(step =>
              <StepsItem key={step.id} step={step} {...actions} />
            )}
          </ul>
          {this.renderFooter(completedCount)}
        </section>
      </div>  
    )
  }
}

export default StepsMain
