<template>
  <div class="section content">
    <div class="columns movies">
      <div class="column">
        <div class="search card">
          <header class="card-header">
            <p class="card-header-title">Wyszukaj film</p>
          </header>
          <div class="card-content">
            <div class="field is-grouped">
              <p class="control is-expanded">
                <input
                  v-model="phrase"
                  class="input"
                  type="text"
                  placeholder="Wpisz tytuł"
                />
              </p>
              <p class="control">
                <button @click="search" class="button is-primary">
                  Szukaj
                </button>
              </p>
            </div>
            <div v-for="result in store.state.search" :key="result.name">
              <MovieCard :record="result" @rate-movie="showRateModal" />
            </div>
            <div v-if="showAdd">
              Nie znaleźliśmy filmu, którego szukasz. Spróbuj go dodać.
              <button class="button is-secondary" @click="showAddModal">
                Dodaj film
              </button>
            </div>
          </div>
        </div>
      </div>
      <div class="column">
        <div class="recommend card">
          <div class="card-header">
            <p class="card-header-title">Rekomendacje</p>
          </div>
          <div class="card-content">
            <div v-for="result in store.state.recommended" :key="result.name">
              <MovieCard :record="result" @rate-movie="showRateModal" />
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="modal" :class="{ 'is-active': rateOpen }">
      <div class="modal-background"></div>
      <div class="modal-card">
        <header class="modal-card-head">
          <h2 class="modal-card-title">
            Oceń film: {{ rated?.name }}({{ rated?.year }})
          </h2>
          <button
            class="delete"
            aria-label="close"
            @click="closeRateModal"
          ></button>
        </header>
        <section v-if="rated" class="modal-card-body">
          <!-- <h3>Tytuł: {{ rated.name }}</h3> -->
          <!-- <h4>Reżyseria: {{ rated.director }}</h4> -->
          <!-- <p>Opis: {{ rated.description }}</p> -->

          <h4>Ocena</h4>
          <input
            id="sliderScore"
            class="slider is-fullwidth is-large has-output"
            min="1"
            max="10"
            v-model="score"
            step="1"
            type="range"
          />
          <output for="sliderScore">{{ score }}</output>
          <div class="columns">
            <div class="column is-3">
              <span class="tag mt-4">Rozśmieszający</span>
            </div>
            <div class="column">
              <input
                id="sliderCat1"
                class="slider is-fullwidth has-output"
                min="1"
                max="10"
                v-model="cat1"
                step="1"
                type="range"
              />
              <output for="sliderCat1">{{ cat1 }}</output>
            </div>
          </div>
          <div class="columns">
            <div class="column is-3">
              <span class="tag mt-4">Inspirujący</span>
            </div>
            <div class="column">
              <input
                id="sliderCat2"
                class="slider is-fullwidth has-output"
                min="1"
                max="10"
                v-model="cat2"
                step="1"
                type="range"
              />
              <output for="sliderCat2">{{ cat2 }}</output>
            </div>
          </div>
          <div class="columns">
            <div class="column is-3">
              <span class="tag mt-4">Zatrważający</span>
            </div>
            <div class="column">
              <input
                id="sliderCat3"
                class="slider is-fullwidth has-output"
                min="1"
                max="10"
                v-model="cat3"
                step="1"
                type="range"
              />
              <output for="sliderCat3">{{ cat3 }}</output>
            </div>
          </div>
          <div class="columns">
            <div class="column is-3">
              <span class="tag mt-4">Niesamowity</span>
            </div>
            <div class="column">
              <input
                id="sliderCat4"
                class="slider is-fullwidth has-output"
                min="1"
                max="10"
                v-model="cat4"
                step="1"
                type="range"
              />
              <output for="sliderCat4">{{ cat4 }}</output>
            </div>
          </div>
          <div class="columns">
            <div class="column is-3">
              <span class="tag mt-4">Oryginalny</span>
            </div>
            <div class="column">
              <input
                id="sliderCat5"
                class="slider is-fullwidth has-output"
                min="1"
                max="10"
                v-model="cat5"
                step="1"
                type="range"
              />
              <output for="sliderCat5">{{ cat5 }}</output>
            </div>
          </div>
          <div class="columns">
            <div class="column is-3">
              <span class="tag mt-4">Zaskakujący</span>
            </div>
            <div class="column">
              <input
                id="sliderCat6"
                class="slider is-fullwidth has-output"
                min="1"
                max="10"
                v-model="cat6"
                step="1"
                type="range"
              />
              <output for="sliderCat6">{{ cat6 }}</output>
            </div>
          </div>
        </section>
        <footer class="modal-card-foot">
          <button class="button is-success" @click="rate">Zapisz</button>
          <button class="button" @click="closeRateModal">Anuluj</button>
        </footer>
      </div>
    </div>
    <div class="modal" :class="{ 'is-active': addOpen }">
      <div class="modal-background"></div>
      <div class="modal-card">
        <header class="modal-card-head">
          <h2 class="modal-card-title">Dodaj film:</h2>
          <button
            class="delete"
            aria-label="close"
            @click="closeAddModal"
          ></button>
        </header>
        <section class="modal-card-body">
          <div class="field is-horizontal">
            <div class="field-label is-normal">
              <label class="label">Tytuł</label>
            </div>
            <div class="field-body">
              <div class="field">
                <p class="control">
                  <input class="input" type="text" v-model="addName" />
                </p>
              </div>
            </div>
          </div>
          <div class="field is-horizontal">
            <div class="field-label is-normal">
              <label class="label">Rok</label>
            </div>
            <div class="field-body">
              <div class="field">
                <p class="control">
                  <input
                    class="input"
                    type="number"
                    min="1800"
                    v-model="addYear"
                  />
                </p>
              </div>
            </div>
          </div>
        </section>
        <footer class="modal-card-foot">
          <button class="button is-success" @click="add">Dodaj</button>
          <button class="button" @click="closeAddModal">Anuluj</button>
        </footer>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import { store, Movie } from "@/store";
import MovieCard from "./MovieCard.vue";

// eslint-disable-next-line @typescript-eslint/no-explicit-any

@Options({
  components: {
    MovieCard,
  },
  props: {},
})
export default class MainPanel extends Vue {
  store = store;
  phrase = "";

  showAdd = false;
  addOpen = false;
  addName = "";
  addYear = 2000;

  rateOpen = false;
  rated: Movie | null = null;
  score = 5;
  cat1 = 5;
  cat2 = 5;
  cat3 = 5;
  cat4 = 5;
  cat5 = 5;
  cat6 = 5;

  async mounted(): Promise<void> {
    try {
      let base = this.store.state.apiUrl;
      let token = this.store.state.token;
      const url = new URL("/recommended", base);
      let recommended = await fetch(url.toString(), {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!recommended.ok) {
        throw new Error(await recommended.text());
      }

      this.store.setRecommended(await recommended.json());
    } catch (e) {
      console.log(e);
    }
  }

  async search(): Promise<void> {
    try {
      this.showAdd = false;
      let base = this.store.state.apiUrl;
      const url = new URL("/search", base);
      url.searchParams.append("searchPhrase", this.phrase);

      let results = await fetch(url.toString());
      let res = await results.json();
      this.store.setSearchResults(res);
      if (Array.isArray(res) && res.length === 0) {
        this.showAdd = true;
      }
    } catch (e) {
      console.log(e);
    }
  }

  async rate(): Promise<void> {
    try {
      let base = this.store.state.apiUrl;
      let token = this.store.state.token;
      const data = {
        movieId: this.rated?.id,
        score: this.score / 10,
        cat1: this.cat1,
        cat2: this.cat2,
        cat3: this.cat3,
        cat4: this.cat4,
        cat5: this.cat5,
        cat6: this.cat6,
      };
      const url = new URL("/rate", base);

      await fetch(url.toString(), {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(data),
      });
      this.closeRateModal();
    } catch (e) {
      console.log(e);
    }
  }

  async add(): Promise<void> {
    try {
      let base = this.store.state.apiUrl;
      const data = {
        name: this.addName,
        year: this.addYear,
      };
      const url = new URL("/add", base);

      await fetch(url.toString(), {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
      });
      this.closeAddModal();
    } catch (e) {
      console.log(e);
    }
  }

  showRateModal(rated: Movie): void {
    this.rated = rated;
    this.rateOpen = true;
  }

  closeRateModal(): void {
    this.rated = null;
    this.score = 0;
    this.cat1 = 0;
    this.cat2 = 0;
    this.cat3 = 0;
    this.cat4 = 0;
    this.cat5 = 0;
    this.cat6 = 0;
    this.rateOpen = false;
  }

  showAddModal(): void {
    this.addOpen = true;
  }

  closeAddModal(): void {
    this.addName = "";
    this.addYear = 2000;
    this.addOpen = false;
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped></style>
