import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import { library } from '@fortawesome/fontawesome-svg-core'
import { 
  faShoppingBag, faUtensils, faCar, faHome, faBox, faShoppingCart, 
  faToiletPaper, faMobileAlt, faMoneyBillWave, faChartLine, faEllipsisH, 
  faWallet, faMedal, faPiggyBank, faUniversity, faChartPie, faHandHoldingUsd, 
  faTags, faCreditCard, faCoffee, faHamburger, faStore, faShoppingBasket
} from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faWeixin, faAlipay } from '@fortawesome/free-brands-svg-icons'

library.add(
  faWeixin, faAlipay,
  faShoppingBag, faUtensils, faCar, faHome, faBox, faShoppingCart,
  faToiletPaper, faMobileAlt, faMoneyBillWave, faChartLine, faEllipsisH,
  faWallet, faMedal, faPiggyBank, faUniversity, faChartPie, faHandHoldingUsd,
  faTags, faCreditCard, faCoffee, faHamburger, faStore, faShoppingBasket
)
const app = createApp(App)

app.use(router)
app.use(ElementPlus,{
    locale: zhCn,
})
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
app.component('font-awesome-icon', FontAwesomeIcon)
app.mount('#app')