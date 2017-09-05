import React, { Component, PropTypes } from 'react'
import classnames from 'classnames'
import StepsTextInput from '../StepsTextInput/StepsTextInput'

export default class StepsItem extends Component {
  static propTypes = {
    step: PropTypes.object.isRequired,
    editStep: PropTypes.func.isRequired,
    deleteStep: PropTypes.func.isRequired,
    completeStep: PropTypes.func.isRequired
  }

  state = {
    editing: false
  }

  handleDoubleClick = () => {
    this.setState({ editing: true })
  }

  handleSave = (id, text) => {
    if (text.length === 0) {
      this.props.deleteStep(id)
    } else {
      this.props.editStep(id, text)
    }
    this.setState({ editing: false })
  }

  render() {
    const { step, completeStep, deleteStep } = this.props

    let element
    if (this.state.editing) {
      element = (
        <StepsTextInput text={step.text}
                       editing={this.state.editing}
                       onSave={(text) => this.handleSave(step.id, text)} />
      )
    } else {
      element = (
        <div className="view">
          <input className="toggle"
                 type="checkbox"
                 checked={step.completed}
                 onChange={() => completeStep(step.id)} />
          <label onDoubleClick={this.handleDoubleClick}>
            {step.text}
          </label>
          <button className="destroy"
                  onClick={() => deleteStep(step.id)} />
        </div>
      )
    }

    return (
      <li className={classnames({
        editing: this.state.editing
      })}>
        {element}
      </li>
    )
  }
}
