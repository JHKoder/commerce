

class Carousel {
  constructor(params) {
    this.carouselContainer = params.container;
    this.controlsContainer = params.controlsContainer;
    this.controlsClassName = this.controlsContainer.className
    this.displayControls = params.displayControls;
    this.carouselControls = ['previous', 'next'];
    this.carouselArray = [...params.items];
    this.classNameItem = this.carouselArray[0].className;
    this.carouselLength = this.carouselArray.length;
    this.textControls = params.textControls;
    this.autoplay = params.autoplay;
    this.autoplayTime = params.autoplayTime;
    this.setParams()
  }
  
    getCurrentState() {
    const selectedItem = this.carouselContainer.querySelector(`.${this.classNameItem}-selected`);
    const previousSelectedItem = this.carouselContainer.querySelector(`.${this.classNameItem}-previous`);
    const nextSelectedItem = this.carouselContainer.querySelector(`.${this.classNameItem}-next`);
    const firstCarouselItem = this.carouselContainer.querySelector(`.${this.classNameItem}-first`);
    const lastCarouselItem = this.carouselContainer.querySelector(`.${this.classNameItem}-last`);

    var indexLastCarouselItem = parseInt(lastCarouselItem.getAttribute('data-index'))
    var indexFirstCarouselItem = parseInt(firstCarouselItem.getAttribute('data-index'))
    var indexDownCarouselItem = indexFirstCarouselItem - 1
    var indexUpCarouselItem = indexLastCarouselItem + 1

    const  downCommingCarouselItem = this.carouselContainer.querySelector(`.${this.classNameItem}[data-index='${indexDownCarouselItem}']`);
    const  upCommingCarouselItem = this.carouselContainer.querySelector(`.${this.classNameItem}[data-index='${indexUpCarouselItem}']`);
    
    return [selectedItem, previousSelectedItem, nextSelectedItem, firstCarouselItem, lastCarouselItem,downCommingCarouselItem, upCommingCarouselItem, indexDownCarouselItem, indexUpCarouselItem]
  }

//모든 매개변수 초기화
  setParams() {
      
    this.setInitialState();
    this.setControls();
    this.onTouch();
      
    if(this.displayControls) {
      this.useControls();
    }
    if(this.autoplay) {
        setInterval( function(){ this.setCurrentState( this.controlsContainer.childNodes[1], this.getCurrentState())}.bind(this), this.autoplayTime)
    }
  }
  
//캐러셀 컨트롤 구성
  setControls() {
    this.carouselControls.forEach((control, index) => {
      this.controlsContainer.appendChild(document.createElement('button')).className = `${this.controlsClassName}-${control}`;
    });
    if(this.displayControls) {
       !!this.controlsContainer.childNodes[0] ? this.controlsContainer.childNodes[0].insertAdjacentHTML('beforeend', this.textControls[0]) : null;
    !!this.controlsContainer.childNodes[1] ? this.controlsContainer.childNodes[1].insertAdjacentHTML('beforeend', this.textControls[1]) : null;
    }
   
  } 
  
// 각 항목에 대한 초기 CSS 클래스 및 속성 할당
  setInitialState() {
    this.carouselArray.forEach((item,index) => {
      item.setAttribute('data-index', index)
    })
    this.carouselArray[0].classList.add(`${this.classNameItem}-first`);
    this.carouselArray[1].classList.add(`${this.classNameItem}-previous`);
    this.carouselArray[2].classList.add(`${this.classNameItem}-selected`);
    this.carouselArray[3].classList.add(`${this.classNameItem}-next`);
    this.carouselArray[4].classList.add(`${this.classNameItem}-last`);
  }

 // CSS 클래스를 사용하여 캐러셀의 주문 상태 업데이트
  setCurrentState(target, [selected, previous, next, first, last, downComming, upComming, downIndex, upIndex]) {

    selected.classList.remove(`${this.classNameItem}-selected`);
    previous.classList.remove(`${this.classNameItem}-previous`);
    next.classList.remove(`${this.classNameItem}-next`);
    first.classList.remove(`${this.classNameItem}-first`);
    last.classList.remove(`${this.classNameItem}-last`);

    if (target.className == `${this.controlsClassName}-${this.carouselControls[1]}`) {
      this.carouselArray.forEach((item) => {
        item.classList.remove(`${this.classNameItem}-trigger-previous`)
        item.classList.add(`${this.classNameItem}-trigger-next`)
      })
      switch(upIndex) {
        case this.carouselLength:
          this.carouselArray[0].classList.add(`${this.classNameItem}-last`);
          previous.classList.add(`${this.classNameItem}-first`);
          selected.classList.add(`${this.classNameItem}-previous`);
          next.classList.add(`${this.classNameItem}-selected`);
          last.classList.add(`${this.classNameItem}-next`);
          break
        case 1:
          this.carouselArray[0].classList.add(`${this.classNameItem}-next`);
          this.carouselArray[1].classList.add(`${this.classNameItem}-last`);
          previous.classList.add(`${this.classNameItem}-first`);
          selected.classList.add(`${this.classNameItem}-previous`);
          next.classList.add(`${this.classNameItem}-selected`);
          break
        case 2:
          this.carouselArray[0].classList.add(`${this.classNameItem}-selected`);
          this.carouselArray[1].classList.add(`${this.classNameItem}-next`);
          this.carouselArray[2].classList.add(`${this.classNameItem}-last`);
          previous.classList.add(`${this.classNameItem}-first`);
          selected.classList.add(`${this.classNameItem}-previous`);
          break
        case 3:
          this.carouselArray[0].classList.add(`${this.classNameItem}-previous`);
          this.carouselArray[1].classList.add(`${this.classNameItem}-selected`);
          this.carouselArray[2].classList.add(`${this.classNameItem}-next`);
          this.carouselArray[3].classList.add(`${this.classNameItem}-last`);
          previous.classList.add(`${this.classNameItem}-first`);
          break
        case 4:
          this.setInitialState()
          break
        default: 
        previous.classList.add(`${this.classNameItem}-first`);
        selected.classList.add(`${this.classNameItem}-previous`);
        next.classList.add(`${this.classNameItem}-selected`);
        last.classList.add(`${this.classNameItem}-next`);
        upComming.classList.add(`${this.classNameItem}-last`)
      }
    } 
    
    else {
       this.carouselArray.forEach((item) => {
        item.classList.remove(`${this.classNameItem}-trigger-next`)
        item.classList.add(`${this.classNameItem}-trigger-previous`)
      })
      switch(downIndex) {
        case -1:
          this.carouselArray[(this.carouselLength -1)].classList.add(`${this.classNameItem}-first`);
          first.classList.add(`${this.classNameItem}-previous`);
          previous.classList.add(`${this.classNameItem}-selected`);
          selected.classList.add(`${this.classNameItem}-next`);
          next.classList.add(`${this.classNameItem}-last`);         
          break
        case (this.CarouselLength - 2):
          this.carouselArray[this.carouselLength -1].classList.add(`${this.classNameItem}-previous`);
          this.carouselArray[this.carouselLength -2].classList.add(`${this.classNameItem}-first`);
          previous.classList.add(`${this.classNameItem}-selected`);
          selected.classList.add(`${this.classNameItem}-next`);
          next.classList.add(`${this.classNameItem}-last`);
          break
        case (this.CarouselLength - 3):
          this.carouselArray[this.carouselLength -1].classList.add(`${this.classNameItem}m-selected`);
          this.carouselArray[this.carouselLength -2].classList.add(`${this.classNameItem}-previous`);
          this.carouselArray[this.carouselLength -3].classList.add(`${this.classNameItem}m-first`);
          selected.classList.add(`${this.classNameItem}-next`);
          next.classList.add(`${this.classNameItem}-last`);
          break
       case (this.CarouselLength - 4):
          this.carouselArray[this.carouselLength -1].classList.add(`${this.classNameItem}-next`);
          this.carouselArray[this.carouselLength -2].classList.add(`${this.classNameItem}m-selected`);
          this.carouselArray[this.carouselLength -3].classList.add(`${this.classNameItem}-previous`);
          this.carouselArray[this.carouselLength -4].classList.add(`${this.classNameItem}-first`);
          selected.classList.add(`${this.classNameItem}-next`);
          next.classList.add(`${this.classNameItem}-last`);
          break
        default:
          downComming.classList.add(`${this.classNameItem}-first`)
          first.classList.add(`${this.classNameItem}-previous`);
          previous.classList.add(`${this.classNameItem}-selected`);
          selected.classList.add(`${this.classNameItem}-next`);
          next.classList.add(`${this.classNameItem}-last`);
      }
    }
  }

   useControls() {
    this.controlsContainer.childNodes.forEach(control => {
      control.addEventListener('click', () => {
        const target = control;
        this.setCurrentState(target, this.getCurrentState());
      });
    });
  }
  
  // 터치 액션
  onTouch() {
    let touchstartX = 0;
    let touchendX = 0;
    let clickX = 0;
    let drag = false;
    
    this.carouselContainer.addEventListener('mousedown', function(event) {
      clickX = event.pageX
      drag = true
    }, false)
    
    this.carouselContainer.addEventListener('mouseup', function(event) {
       if(drag) {
         if(event.pageX < clickX) {
            this.setCurrentState( this.controlsContainer.childNodes[1], this.getCurrentState())
          } else if (event.pageX > clickX) {
            this.setCurrentState( this.controlsContainer.childNodes[0], this.getCurrentState())
          }
       }
    }.bind(this), false)
 this.carouselContainer.addEventListener('touchstart', function(event) {
        touchstartX = event.changedTouches[0].screenX;
    }, false);
    this.carouselContainer.addEventListener('touchend', function(event) {
        touchendX = event.changedTouches[0].screenX;
        if (touchendX <= touchstartX) {
            this.setCurrentState( this.controlsContainer.childNodes[1], this.getCurrentState())
        }
        else if (touchendX > touchstartX) {
            this.setCurrentState( this.controlsContainer.childNodes[0], this.getCurrentState())
        }
    }.bind(this), false); 
  }

}
