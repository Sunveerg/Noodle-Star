import React, { useState, ChangeEvent, FormEvent } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import axios from 'axios';
import { Status } from '../features/model/Status'; // Ensure this is correctly imported
import { menuRequestModel } from '../features/model/menuRequestModel'; // Ensure this is correctly imported

const AddDish: React.FC = () => {
  const [dish, setDish] = useState<menuRequestModel>({
    name: '',
    description: '',
    price: 0,
    category: '',
    itemImage: '',
    status: Status.AVAILABLE,
  });
  const [errors, setErrors] = useState<{ [key: string]: string }>({});
  const [showModal, setShowModal] = useState(false);
  const backendUrl = process.env.REACT_APP_BACKEND_URL;

  const handleChange = (
    e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ): void => {
    const { name, value } = e.target;
    setDish({ ...dish, [name]: value });
  };

  const validate = (): boolean => {
    const newErrors: { [key: string]: string } = {};
    if (!dish.name) newErrors.name = 'Dish name is required';
    if (!dish.description) newErrors.description = 'Description is required';
    if (!dish.price || dish.price <= 0)
      newErrors.price = 'Price must be a positive number';
    if (!dish.category) newErrors.category = 'Category is required';
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!validate()) return;

    const dishPayload = {
      name: dish.name,
      description: dish.description,
      price: dish.price,
      category: dish.category,
      itemImage: dish.itemImage,
      status: dish.status,
    };

    try {
      await axios.post(`${backendUrl}/api/v1/menu`, dishPayload);
      setShowModal(false); // Close the modal
      window.location.reload(); // Reload the page to reflect the changes
    } catch (error) {
      console.error('Failed to add dish:', error);
    }
  };

  return (
    <>
      <Button
        onClick={() => setShowModal(true)}
        variant="warning"
        style={{ marginBottom: '20px' }}
      >
        +
      </Button>

      <Modal
        show={showModal}
        onHide={() => setShowModal(false)}
        backdrop="static"
        keyboard={false}
      >
        <Modal.Header closeButton>
          <Modal.Title>Add Dish</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form id="addDishForm" onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
              <Form.Label>Dish Name</Form.Label>
              <Form.Control
                type="text"
                name="name"
                value={dish.name}
                onChange={handleChange}
                isInvalid={!!errors.name}
              />
              <Form.Control.Feedback type="invalid">
                {errors.name}
              </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Description</Form.Label>
              <Form.Control
                as="textarea"
                name="description"
                value={dish.description}
                onChange={handleChange}
                isInvalid={!!errors.description}
              />
              <Form.Control.Feedback type="invalid">
                {errors.description}
              </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Price</Form.Label>
              <Form.Control
                type="number"
                name="price"
                value={dish.price}
                onChange={handleChange}
                isInvalid={!!errors.price}
              />
              <Form.Control.Feedback type="invalid">
                {errors.price}
              </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Category</Form.Label>
              <Form.Control
                type="text"
                name="category"
                value={dish.category}
                onChange={handleChange}
                isInvalid={!!errors.category}
              />
              <Form.Control.Feedback type="invalid">
                {errors.category}
              </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Image URL</Form.Label>
              <Form.Control
                type="text"
                name="itemImage"
                value={dish.itemImage}
                onChange={handleChange}
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Close
          </Button>
          <Button form="addDishForm" variant="primary" type="submit">
            Add Dish
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default AddDish;
