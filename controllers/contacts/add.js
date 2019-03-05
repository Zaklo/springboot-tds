import Controller from '@ember/controller';

export default Controller.extend({
  actions: {
    addContact(contact){
      contact.save();
    }

  }
});
