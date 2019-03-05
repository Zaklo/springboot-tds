import Route from '@ember/routing/route';
import EmberObject from 'ember/object';


let Contacts = EmberObject.extend({})

export default Route.extend({
  model() {
    //return la liste des contacts depuis la BDD
    return Contacts.create(this.store.findAll('contact'));
  }
});
