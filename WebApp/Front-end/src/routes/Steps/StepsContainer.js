import { connect } from 'react-redux'
import { increment, doubleAsync } from './StepsModule'
import Steps from './Steps'


const mapDispatchToProps = {
  increment : () => increment(1),
  doubleAsync
}

const mapStateToProps = (state) => ({
  steps : state.steps
})

export default connect(mapStateToProps, mapDispatchToProps)(Steps)
