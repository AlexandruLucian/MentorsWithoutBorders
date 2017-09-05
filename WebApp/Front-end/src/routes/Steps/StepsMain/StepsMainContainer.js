import { connect } from 'react-redux'
import { increment, doubleAsync } from './StepsMainModule'
import StepsMain from './StepsMain'


const mapDispatchToProps = {
  addStep : (text) => addStep(text)
}

const mapStateToProps = (state) => ({
  steps : state.steps
})

export default connect(mapStateToProps, mapDispatchToProps)(StepsMain)
