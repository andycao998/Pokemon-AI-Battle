class EventBus extends EventTarget {
    emit(event, detail = {}) {
      this.dispatchEvent(new CustomEvent(event, { detail }));
    }
  
    on(event, callback) {
      this.addEventListener(event, callback);
    }
  
    off(event, callback) {
      this.removeEventListener(event, callback);
    }
  }
  
  const eventBus = new EventBus();

  export default eventBus;