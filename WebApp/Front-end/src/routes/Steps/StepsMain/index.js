import { injectReducer } from '../../../store/reducers'

export default (store) => ({
  path : 'steps',
  /*  Async getComponent is only invoked when route matches   */
  getComponent (nextState, cb) {
    /*  Webpack - use 'require.ensure' to create a split point
        and embed an async module loader (jsonp) when bundling   */
    require.ensure([], (require) => {
      /*  Webpack - use require callback to define
          dependencies for bundling   */
      const StepsMain= require('./StepsMainContainer').default
      const reducer = require('./StepsMainModule').default

      /*  Add the reducer to the store on key 'steps'  */
      injectReducer(store, { key: 'steps', reducer })

      /*  Return getComponent   */
      cb(null, StepsMain)

    /* Webpack named bundle   */
    }, 'steps')
  }
})
