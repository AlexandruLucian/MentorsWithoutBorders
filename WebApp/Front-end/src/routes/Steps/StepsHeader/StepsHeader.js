import React, { PropTypes, Component } from 'react'
import StepsTextInput from '../StepsTextInput/StepsTextInput'

export default class StepsHeader extends Component {
  static propTypes = {
    addStep: PropTypes.func.isRequired
  }

  handleSave = text => {
    if (text.length !== 0) {
      this.props.addStep(text)
    }
  }

  render() {
    return (
      <header className="header">
        <h1>Mentoring Plan</h1>
        <StepsTextInput newStep
                        onSave={this.handleSave}
                        placeholder="What is the next step?" />
      </header>
    )
  }
}
