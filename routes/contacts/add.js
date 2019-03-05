import Route from '@ember/routing/route';

export default Route.extend({
  model() {
    let contact = this.get('store').createRecord(
      {nom: 'SMITH', prenom: 'Antoine', email: 'lozachantoine@gmail.com'}
    );
  }
});
